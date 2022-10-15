package ru.artemev.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import ru.artemev.deal.dto.EmploymentDTO;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.entity.CreditEntity;
import ru.artemev.deal.mapper.ApplicationEntityMapper;
import ru.artemev.deal.mapper.ClientEntityMapper;
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.EmailMessage;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.model.enums.CreditStatus;
import ru.artemev.deal.model.enums.EmploymentStatus;
import ru.artemev.deal.model.enums.Gender;
import ru.artemev.deal.model.enums.MaritalStatus;
import ru.artemev.deal.model.enums.Position;
import ru.artemev.deal.model.enums.Theme;
import ru.artemev.deal.repository.ApplicationRepository;
import ru.artemev.deal.repository.ClientRepository;
import ru.artemev.deal.repository.CreditRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class DealServiceImplTest {

  @Autowired private DealService dealService;

  @Autowired private ClientRepository clientRepository;

  @Autowired private ApplicationRepository applicationRepository;

  @Autowired private CreditRepository creditRepository;

  @MockBean
  private KafkaTemplate<Long, EmailMessage> kafkaTemplate;

  @Test
  void calculationPossibleLoans() {
    LoanApplicationRequestDTO loanApplicationRequestDTO =
        LoanApplicationRequestDTO.builder()
            .firstName("Петя")
            .lastName("Петров")
            .middleName("Петрович")
            .term(12)
            .amount(BigDecimal.valueOf(100_000))
            .email("example@test.ru")
            .birthday(LocalDate.of(2000, 1, 1))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    ClientEntity clientEntity = ClientEntityMapper.toClientEntity(loanApplicationRequestDTO);
    ApplicationEntity applicationEntity = ApplicationEntityMapper.toApplicationEntity(clientEntity);

    clientEntity.setId(1L);
    applicationEntity.setId(1L);

    assertEquals(loanApplicationRequestDTO.getFirstName(), clientEntity.getFirstName());
    assertEquals(loanApplicationRequestDTO.getLastName(), clientEntity.getLastName());
    assertEquals(loanApplicationRequestDTO.getMiddleName(), clientEntity.getMiddleName());
    assertEquals(loanApplicationRequestDTO.getBirthday(), clientEntity.getBirthday());
    assertEquals(loanApplicationRequestDTO.getEmail(), clientEntity.getEmail());
    assertEquals(
        loanApplicationRequestDTO.getPassportNumber(), clientEntity.getPassport().getNumber());
    assertEquals(
        loanApplicationRequestDTO.getPassportSeries(), clientEntity.getPassport().getSeries());

    clientRepository.save(clientEntity);
    applicationRepository.save(applicationEntity);

    assertNotNull(clientRepository.findById(clientEntity.getId()));
    assertNotNull(applicationRepository.findById(applicationEntity.getId()));

    List<LoanOfferDTO> loanOfferDTOList =
        List.of(
            LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(115000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9583.33))
                .rate(BigDecimal.valueOf(15))
                .insuranceEnabled(false)
                .salaryClient(false)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(112000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9333.33))
                .rate(BigDecimal.valueOf(12))
                .insuranceEnabled(true)
                .salaryClient(false)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(111040))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9253.33))
                .rate(BigDecimal.valueOf(11))
                .insuranceEnabled(false)
                .salaryClient(true)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(108040))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9003.33))
                .rate(BigDecimal.valueOf(8))
                .insuranceEnabled(true)
                .salaryClient(true)
                .build());

    assertEquals(loanOfferDTOList, dealService.calculationPossibleLoans(loanApplicationRequestDTO));
  }

  @Test
  void selectOneOfOffers() {

    LoanApplicationRequestDTO loanApplicationRequestDTO =
        LoanApplicationRequestDTO.builder()
            .firstName("Петя")
            .lastName("Петров")
            .middleName("Петрович")
            .term(12)
            .amount(BigDecimal.valueOf(100_000))
            .email("example@test.ru")
            .birthday(LocalDate.of(2000, 1, 1))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    List<LoanOfferDTO> loanOfferDTOList =
        dealService.calculationPossibleLoans(loanApplicationRequestDTO);

    assertNotNull(loanOfferDTOList);

    dealService.selectOneOfOffers(loanOfferDTOList.get(0));

    ApplicationEntity applicationEntity =
        applicationRepository.findById(loanOfferDTOList.get(0).getApplicationId()).get();

    assertNotNull(applicationEntity);

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();
    assertNotNull(applicationHistoryList);
    assertEquals(applicationHistoryList.get(0).getStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(applicationHistoryList.get(0).getDate(), LocalDate.now());
    assertEquals(applicationEntity.getApplicationStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(applicationEntity.getAppliedOffer(), loanOfferDTOList.get(0));
    verify(kafkaTemplate, VerificationModeFactory.times(1)).send("conveyor-create-documents", 2L, new EmailMessage("example@test.ru", Theme.CREATE_DOCUMENTS, 2L));

  }

  @Test
  void completionOfRegistration() {
    LoanApplicationRequestDTO loanApplicationRequestDTO =
        LoanApplicationRequestDTO.builder()
            .firstName("Петя")
            .lastName("Петров")
            .middleName("Петрович")
            .term(12)
            .amount(BigDecimal.valueOf(100_000))
            .email("example@test.ru")
            .birthday(LocalDate.of(2000, 1, 1))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    FinishRegistrationRequestDTO finishRegistrationRequestDTO =
        FinishRegistrationRequestDTO.builder()
            .gender(Gender.MALE)
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .passportIssueDate(LocalDate.of(2000, 1, 1))
            .passportIssueBranch("Moscow")
            .employment(
                EmploymentDTO.builder()
                    .salary(BigDecimal.valueOf(15000))
                    .position(Position.MID_MANAGER)
                    .workExperienceTotal(12)
                    .workExperienceCurrent(3)
                    .employerINN("123456")
                    .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                    .build())
            .account("testAccount")
            .build();

    LoanOfferDTO loanOfferDTO =
        dealService.calculationPossibleLoans(loanApplicationRequestDTO).get(0);

    Long applicationId = loanOfferDTO.getApplicationId();
    dealService.selectOneOfOffers(loanOfferDTO);
    dealService.completionOfRegistration(applicationId, finishRegistrationRequestDTO);

    ApplicationEntity applicationEntity = applicationRepository.findById(applicationId).get();
    assertNotNull(applicationEntity);

    CreditEntity creditEntity = applicationEntity.getCreditEntity();

    assertNotNull(creditEntity);

    assertEquals(creditEntity.getCreditStatus(), CreditStatus.CALCULATED);
    assertEquals(applicationEntity.getApplicationStatus(), ApplicationStatus.CC_APPROVED);

    verify(kafkaTemplate, VerificationModeFactory.times(1)).send("conveyor-finish-registration", 2L, new EmailMessage("example@test.ru", Theme.FINISH_REGISTRATION, 2L));
  }
}
