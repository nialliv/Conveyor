package ru.artemev.deal.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemev.deal.aspects.AuditAction;
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
import ru.artemev.deal.mapper.ClientEntityMapper;
import ru.artemev.deal.mapper.CreditEntityMapper;
import ru.artemev.deal.mapper.ScoringDataDTOMapper;
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.EmailMessage;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.model.enums.CreditStatus;
import ru.artemev.deal.model.enums.Theme;
import ru.artemev.deal.repository.ApplicationRepository;
import ru.artemev.deal.repository.ClientRepository;
import ru.artemev.deal.repository.CreditRepository;
import ru.artemev.deal.service.DealService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

  private final ApplicationRepository applicationRepository;

  private final CreditRepository creditRepository;

  private final ClientRepository clientRepository;

  private final ConveyorClient conveyorClient;

  private final ClientEntityMapper clientEntityMapper;

  private final CreditEntityMapper creditEntityMapper;

  private final ScoringDataDTOMapper scoringDataDTOMapper;

  private final KafkaTemplate<Long, EmailMessage> kafkaTemplate;

  private final Counter statusCounter = Metrics.counter("documents_status_total");

  @Override
  @Transactional
  @AuditAction
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
        clientRepository.save(clientEntityMapper.toClientEntity(loanApplicationRequestDTO));
    log.info("Created clientEntity = " + clientEntity);

    ApplicationEntity applicationEntity =
        applicationRepository.save(
            ApplicationEntity.builder()
                .clientEntity(clientEntity)
                .creationDate(LocalDate.now())
                .build());
    log.info("Created applicationEntity = " + applicationEntity);

    List<LoanOfferDTO> loanOfferDTOList = conveyorClient.getOffers(loanApplicationRequestDTO);
    log.info("Received request from conveyor service = " + loanOfferDTOList);

    if (loanOfferDTOList != null)
      loanOfferDTOList.forEach(
          loanOfferDTO -> loanOfferDTO.setApplicationId(applicationEntity.getId()));

    log.info("======Finished calculationPossibleLoans=======");
    return loanOfferDTOList;
  }

  @Override
  @Transactional
  @AuditAction
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

    statusCounter.increment();

    applicationRepository.save(applicationEntity);

    kafkaTemplate.send(
        "conveyor-finish-registration",
        applicationEntity.getId(),
        new EmailMessage(
            applicationEntity.getClientEntity().getEmail(),
            Theme.FINISH_REGISTRATION,
            applicationEntity.getId()));

    log.info("======Finished selectOneOfOffers=======");
  }

  @Override
  @Transactional
  @AuditAction
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

    clientEntityMapper.update(clientEntity, finishRegistrationRequestDTO);
    applicationEntity.setClientEntity(clientEntity);
    log.info("Fielded clientEntity");

    ScoringDataDTO scoringDataDTO = scoringDataDTOMapper.toScoringDataDTO(applicationEntity);
    log.info("Fielded scoringDataDTO = " + scoringDataDTO);

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();

    applicationHistoryList.add(
        ApplicationHistory.builder()
            .date(LocalDate.now())
            .status(ApplicationStatus.PREPARE_DOCUMENTS)
            .build());

    applicationEntity.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
    applicationEntity.setStatusHistory(applicationHistoryList);

    statusCounter.increment();

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

    CreditEntity creditEntity = creditEntityMapper.toCreditEntity(creditDTO);
    creditEntity.setCreditStatus(CreditStatus.CALCULATED);
    creditRepository.save(creditEntity);

    applicationEntity.setCreditEntity(creditEntity);

    log.info("Saved creditEntity");

    applicationHistoryList.add(
        ApplicationHistory.builder()
            .date(LocalDate.now())
            .status(ApplicationStatus.CC_APPROVED)
            .build());

    applicationEntity.setApplicationStatus(ApplicationStatus.CC_APPROVED);
    applicationEntity.setStatusHistory(applicationHistoryList);

    statusCounter.increment();

    log.info("Updated applicationStatus");

    clientRepository.save(clientEntity);
    log.info("Saved clientEntity");
    applicationRepository.save(applicationEntity);
    log.info("Saved applicationEntity");

    kafkaTemplate.send(
        "conveyor-create-documents",
        applicationEntity.getId(),
        new EmailMessage(
            applicationEntity.getClientEntity().getEmail(),
            Theme.CREATE_DOCUMENTS,
            applicationEntity.getId()));

    log.info("====== Finished completionOfRegistration =======");
  }

  @Override
  public void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus) {
    log.info("====== Started updateApplicationStatus =======");
    log.info(
        "Received applicationId -> "
            + applicationId
            + "; with applicationStatus -> "
            + applicationStatus);

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new NotFoundException(applicationId + " not found in application repository"));
    applicationEntity.setApplicationStatus(applicationStatus);
    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();
    applicationHistoryList.add(
        ApplicationHistory.builder().date(LocalDate.now()).status(applicationStatus).build());
    applicationEntity.setStatusHistory(applicationHistoryList);

    statusCounter.increment();
    applicationRepository.save(applicationEntity);

    log.info("====== Finished updateApplicationStatus =======");
  }

  @Override
  @AuditAction
  public void sendDocuments(Long applicationId) {
    log.info("====== Started sendDocuments =======");

    updateApplicationStatus(applicationId, ApplicationStatus.PREPARE_DOCUMENTS);
    statusCounter.increment();
    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new NotFoundException(applicationId + " not found in application repository"));
    kafkaTemplate.send(
        "conveyor-send-documents",
        applicationId,
        new EmailMessage(
            applicationEntity.getClientEntity().getEmail(), Theme.SEND_DOCUMENTS, applicationId));

    log.info("====== Finished sendDocuments =======");
  }

  @Override
  @AuditAction
  public void signDocuments(Long applicationId) {
    log.info("====== Started signDocuments =======");

    Integer sesCode = ThreadLocalRandom.current().nextInt(1000, 10_001);
    log.info("Generated sesCode -> " + sesCode);

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new NotFoundException(applicationId + " not found in application repository"));
    applicationEntity.setSesCode(String.valueOf(sesCode));
    applicationRepository.save(applicationEntity);

    kafkaTemplate.send(
        "conveyor-sign-documents",
        applicationId,
        new EmailMessage(
            applicationEntity.getClientEntity().getEmail(), Theme.SIGN_DOCUMENTS, applicationId));

    log.info("====== Finished signDocuments =======");
  }

  @Override
  @AuditAction
  public void codeDocuments(Long applicationId, Integer sesCode) {
    log.info("====== Started codeDocuments =======");

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new NotFoundException(applicationId + " not found in application repository"));
    CreditEntity creditEntity = applicationEntity.getCreditEntity();

    applicationEntity.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();
    applicationHistoryList.add(
        ApplicationHistory.builder()
            .date(LocalDate.now())
            .status(ApplicationStatus.DOCUMENT_SIGNED)
            .build());
    applicationEntity.setStatusHistory(applicationHistoryList);

    creditEntity.setCreditStatus(CreditStatus.ISSUED);

    statusCounter.increment();
    creditRepository.save(creditEntity);
    applicationRepository.save(applicationEntity);

    kafkaTemplate.send(
        "conveyor-credit",
        applicationId,
        new EmailMessage(
            applicationEntity.getClientEntity().getEmail(), Theme.CREDIT_ISSUED, applicationId));

    log.info("====== Finished codeDocuments =======");
  }
}
