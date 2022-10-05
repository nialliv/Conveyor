package ru.artemev.deal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.Passport;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.model.enums.CreditStatus;
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
        clientRepository.save(loanApplicationRequestDTOToClientEntity(loanApplicationRequestDTO));
    log.info("Created clientEntity = " + clientEntity);
    ApplicationEntity applicationEntity =
        applicationRepository.save(loanApplicationRequestDTOToApplicationEntity(clientEntity));
    log.info("Created applicationEntity = " + applicationEntity);
    List<LoanOfferDTO> result = conveyorClient.getOffers(loanApplicationRequestDTO).getBody();
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
            .orElseThrow(RuntimeException::new);

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
        applicationRepository.findById(id).orElseThrow(RuntimeException::new);
    ClientEntity clientEntity = applicationEntity.getClientEntity();
    LoanOfferDTO loanOfferDTO = applicationEntity.getAppliedOffer();
    log.info("Received all Entity");

    clientEntity.setGender(finishRegistrationRequestDTO.getGender());
    clientEntity.setAccount(finishRegistrationRequestDTO.getAccount());
    clientEntity.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
    clientEntity.setEmployment(finishRegistrationRequestDTO.getEmployment());
    clientEntity.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
    clientEntity.setPassport(
        Passport.builder()
            .number(clientEntity.getPassport().getNumber())
            .series(clientEntity.getPassport().getSeries())
            .issueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
            .issueDate(finishRegistrationRequestDTO.getPassportIssueDate())
            .build());

    log.info("Fielded clientEntity");

    ScoringDataDTO scoringDataDTO =
        ScoringDataDTO.builder()
            .amount(loanOfferDTO.getTotalAmount())
            .term(loanOfferDTO.getTerm())
            .firstName(clientEntity.getFirstName())
            .lastName(clientEntity.getLastName())
            .middleName(clientEntity.getMiddleName())
            .gender(clientEntity.getGender())
            .birthday(clientEntity.getBirthday())
            .passportSeries(clientEntity.getPassport().getSeries())
            .passportNumber(clientEntity.getPassport().getNumber())
            .passportIssueBranch(clientEntity.getPassport().getIssueBranch())
            .passportIssueDate(clientEntity.getPassport().getIssueDate())
            .maritalStatus(clientEntity.getMaritalStatus())
            .dependentAmount(clientEntity.getDependentAmount())
            .employment(clientEntity.getEmployment())
            .account(clientEntity.getAccount())
            .insuranceEnabled(loanOfferDTO.isInsuranceEnabled())
            .salaryClient(loanOfferDTO.isSalaryClient())
            .build();

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
      throw new RuntimeException("CreditDTO received as null");
    }

    CreditEntity creditEntity =
        creditRepository.save(
            CreditEntity.builder()
                .amount(creditDTO.getAmount())
                .term(creditDTO.getTerm())
                .monthlyPayment(creditDTO.getMonthlyPayment())
                .rate(creditDTO.getRate())
                .psk(creditDTO.getPsk())
                .paymentSchedule(creditDTO.getPaymentSchedule())
                .insuranceEnabled(creditDTO.getInsuranceEnabled())
                .salaryClient(creditDTO.getSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build());

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

  private ApplicationEntity loanApplicationRequestDTOToApplicationEntity(
      ClientEntity clientEntity) {
    return ApplicationEntity.builder()
        .clientEntity(clientEntity)
        .creationDate(LocalDate.now())
        .build();
  }

  private ClientEntity loanApplicationRequestDTOToClientEntity(
      LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return ClientEntity.builder()
        .firstName(loanApplicationRequestDTO.getFirstName())
        .lastName(loanApplicationRequestDTO.getLastName())
        .middleName(loanApplicationRequestDTO.getMiddleName())
        .birthday(loanApplicationRequestDTO.getBirthday())
        .email(loanApplicationRequestDTO.getEmail())
        .passport(
            Passport.builder()
                .series(loanApplicationRequestDTO.getPassportSeries())
                .number(loanApplicationRequestDTO.getPassportNumber())
                .build())
        .build();
  }
}
