package ru.artemev.deal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemev.deal.client.ConveyorClient;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.dto.ScoringDataDTO;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.entity.CreditEntity;
import ru.artemev.deal.exception.ApiError;
import ru.artemev.deal.exception.BaseException;
import ru.artemev.deal.exception.NotFoundException;
import ru.artemev.deal.mapper.ApplicationEntityMapper;
import ru.artemev.deal.mapper.ClientEntityMapper;
import ru.artemev.deal.mapper.CreditEntityMapper;
import ru.artemev.deal.mapper.ScoringDataDTOMapper;
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.repository.ApplicationRepository;
import ru.artemev.deal.repository.ClientRepository;
import ru.artemev.deal.repository.CreditRepository;
import ru.artemev.deal.service.DealService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class DealServiceImpl implements DealService {

  @Autowired private ApplicationRepository applicationRepository;

  @Autowired private CreditRepository creditRepository;

  @Autowired private ClientRepository clientRepository;

  @Autowired private ConveyorClient conveyorClient;

  @Override
  @Transactional
  public List<LoanOfferDTO> calculationPossibleLoans(
      LoanApplicationRequestDTO loanApplicationRequestDTO) {
    /*
    По API приходит LoanApplicationRequestDTO
    На основе LoanApplicationRequestDTO создаётся сущность Client и сохраняется в БД.
    Создаётся Application со связью на только что созданный Client и сохраняется в БД.
    Отправляется POST запрос на /conveyor/offers МС conveyor через FeignClient. Каждому элементу из списка List<LoanOfferDTO> присваивается id созданной заявки (Application)
    Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему".
     */
    log.info("======Started calculationPossibleLoans=======");
    log.info("Received request = " + loanApplicationRequestDTO);

    ClientEntity clientEntity =
        clientRepository.save(ClientEntityMapper.toClientEntity(loanApplicationRequestDTO));
    log.info("Created clientEntity = " + clientEntity);

    ApplicationEntity applicationEntity =
        applicationRepository.save(ApplicationEntityMapper.toApplicationEntity(clientEntity));
    log.info("Created applicationEntity = " + applicationEntity);

    List<LoanOfferDTO> result = conveyorClient.getOffers(loanApplicationRequestDTO);
    log.info("Received request from conveyor service = " + result);

    if (result != null) {
      result.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(applicationEntity.getId()));
    }

    log.info("======Finished calculationPossibleLoans=======");
    return result;
  }

  @Override
  @Transactional
  public void selectOneOfOffers(LoanOfferDTO loanOfferDTO) {
    /*
    По API приходит LoanOfferDTO
    Достаётся из БД заявка(Application) по applicationId из LoanOfferDTO.
    В заявке обновляется статус, история статусов(List<ApplicationStatusHistoryDTO>), принятое предложение LoanOfferDTO устанавливается в поле appliedOffer.
    Заявка сохраняется.
     */
    log.info("======Started selectOneOfOffers=======");
    log.info("Received request = " + loanOfferDTO);

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(loanOfferDTO.getApplicationId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        loanOfferDTO.getApplicationId() + " not found in application repository"));

    List<ApplicationHistory> applicationHistory = applicationEntity.getStatusHistory();
    if (applicationHistory == null) {
      applicationHistory =
          List.of(
              ApplicationHistory.builder()
                  .status(ApplicationStatus.PREAPPROVAL)
                  .date(LocalDate.now())
                  .build());

    } else {
      applicationHistory.add(
          ApplicationHistory.builder()
              .status(ApplicationStatus.PREAPPROVAL)
              .date(LocalDate.now())
              .build());
    }

    applicationEntity.setStatusHistory(applicationHistory);
    applicationEntity.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
    applicationEntity.setAppliedOffer(loanOfferDTO);

    applicationRepository.save(applicationEntity);

    log.info("======Finished selectOneOfOffers=======");
  }

  @Override
  @Transactional
  public void completionOfRegistration(
      Long id, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
    /*
    По API приходит объект FinishRegistrationRequestDTO и параметр applicationId (Long).
    Достаётся из БД заявка(Application) по applicationId.
    ScoringDataDTO насыщается информацией из FinishRegistrationRequestDTO и Client, который хранится в Application
    Отправляется POST запрос на /conveyor/calculation МС conveyor с телом ScoringDataDTO через FeignClient.
    На основе полученного из кредитного конвейера CreditDTO создаётся сущность Credit и сохраняется в базу со статусом CALCULATED.
    В заявке обновляется статус, история статусов.
    Заявка сохраняется.
     */
    log.info("====== Started completionOfRegistration =======");

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id + " not found in application repository"));

    ClientEntity clientEntity = applicationEntity.getClientEntity();

    log.info("Received all Entity");

    ClientEntityMapper.fieldClientEntity(clientEntity, finishRegistrationRequestDTO);
    applicationEntity.setClientEntity(clientEntity);
    log.info("Fielded clientEntity");

    ScoringDataDTO scoringDataDTO = ScoringDataDTOMapper.toScoringDataDTO(applicationEntity);
    log.info("Fielded scoringDataDTO = " + scoringDataDTO);

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();

    applicationHistoryList.add(
        ApplicationHistory.builder()
            .date(LocalDate.now())
            .status(ApplicationStatus.PREPARE_DOCUMENTS)
            .build());

    applicationEntity.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
    applicationEntity.setStatusHistory(applicationHistoryList);

    log.info("Updated applicationStatus = " + applicationEntity.getApplicationStatus());
    log.info("Updated applicationHistoryList = " + applicationHistoryList);

    CreditDTO creditDTO = conveyorClient.getCreditDto(scoringDataDTO);

    log.info("Received creditDTO from conveyor-client");
    log.info("creditDTO = " + creditDTO);

    if (creditDTO == null) {
      throw new BaseException(
          HttpStatus.BAD_REQUEST,
              new ApiError(this.getClass().toString(), "CreditDTO received as null"));
    }

    CreditEntity creditEntity = creditRepository.save(CreditEntityMapper.toClientEntity(creditDTO));
    applicationEntity.setCreditEntity(creditEntity);

    log.info("Saved creditEntity");

    applicationHistoryList.add(
        ApplicationHistory.builder()
            .date(LocalDate.now())
            .status(ApplicationStatus.CC_APPROVED)
            .build());

    applicationEntity.setApplicationStatus(ApplicationStatus.CC_APPROVED);
    applicationEntity.setStatusHistory(applicationHistoryList);

    log.info("Updated applicationStatus");

    clientRepository.save(clientEntity);
    log.info("Saved clientEntity");
    applicationRepository.save(applicationEntity);
    log.info("Saved applicationEntity");
    log.info("====== Finished completionOfRegistration =======");
  }
}
