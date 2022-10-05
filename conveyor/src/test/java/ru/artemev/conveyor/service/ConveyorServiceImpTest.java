package ru.artemev.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.EmploymentDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.model.PaymentScheduleElement;
import ru.artemev.conveyor.model.enums.EmploymentStatus;
import ru.artemev.conveyor.model.enums.Gender;
import ru.artemev.conveyor.model.enums.MaritalStatus;
import ru.artemev.conveyor.model.enums.Position;
import ru.artemev.conveyor.service.impl.ConveyorServiceImp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ConveyorServiceImpTest {

  @Autowired ConveyorService conveyorService = new ConveyorServiceImp();

  @Test
  void getOffers() {
    LoanApplicationRequestDTO loanApplicationRequestDTO = mock(LoanApplicationRequestDTO.class);

    when(loanApplicationRequestDTO.getAmount()).thenReturn(BigDecimal.valueOf(100_000));
    when(loanApplicationRequestDTO.getBirthday()).thenReturn(LocalDate.of(2000, 1, 1));
    when(loanApplicationRequestDTO.getTerm()).thenReturn(12);

    List<LoanOfferDTO> loanOfferDTOList = conveyorService.getOffers(loanApplicationRequestDTO);

    List<LoanOfferDTO> loanOfferDTOListTest =
        List.of(
            LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(115000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9583.33))
                .rate(BigDecimal.valueOf(15))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(112000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9333.33))
                .rate(BigDecimal.valueOf(12))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(111040))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9253.33))
                .rate(BigDecimal.valueOf(11))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build(),
            LoanOfferDTO.builder()
                .applicationId(4L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(108040))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9003.33))
                .rate(BigDecimal.valueOf(8))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build());

    assertEquals(loanOfferDTOList, loanOfferDTOListTest);
  }

  @Test
  void getCreditDto() {

    ScoringDataDTO scoringDataDTO = mock(ScoringDataDTO.class);

    when(scoringDataDTO.getAmount()).thenReturn(BigDecimal.valueOf(100000));
    when(scoringDataDTO.getTerm()).thenReturn(3);
    when(scoringDataDTO.getGender()).thenReturn(Gender.MALE);
    when(scoringDataDTO.getBirthday()).thenReturn(LocalDate.of(2000, 10, 3));
    when(scoringDataDTO.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);
    when(scoringDataDTO.getDependentAmount()).thenReturn(1);
    when(scoringDataDTO.getEmployment())
        .thenReturn(
            EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .salary(BigDecimal.valueOf(15000))
                .position(Position.MID_MANAGER)
                .workExperienceTotal(12)
                .workExperienceCurrent(3)
                .build());
    when(scoringDataDTO.getIsInsuranceEnabled()).thenReturn(true);
    when(scoringDataDTO.getIsSalaryClient()).thenReturn(true);

    CreditDTO creditDTO = conveyorService.getCreditDto(scoringDataDTO);

    CreditDTO creditDTOTest =
        CreditDTO.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(3)
            .monthlyPayment(BigDecimal.valueOf(33663.33))
            .rate(BigDecimal.valueOf(4))
            .psk(BigDecimal.valueOf(100990))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .paymentSchedule(
                List.of(
                    PaymentScheduleElement.builder()
                        .number(1)
                        .date(LocalDate.of(2022, 11, 3))
                        .totalPayment(BigDecimal.valueOf(33663.33))
                        .interestPayment(BigDecimal.valueOf(1333.33))
                        .debtPayment(BigDecimal.valueOf(33333.33))
                        .remainingDebt(BigDecimal.valueOf(67326.67))
                        .build(),
                    PaymentScheduleElement.builder()
                        .number(2)
                        .date(LocalDate.of(2022, 12, 3))
                        .totalPayment(BigDecimal.valueOf(67326.66))
                        .interestPayment(BigDecimal.valueOf(1333.33))
                        .debtPayment(BigDecimal.valueOf(33333.33))
                        .remainingDebt(BigDecimal.valueOf(33663.34))
                        .build(),
                    PaymentScheduleElement.builder()
                        .number(3)
                        .date(LocalDate.of(2023, 1, 3))
                        .totalPayment(BigDecimal.valueOf(100989.99))
                        .interestPayment(BigDecimal.valueOf(1333.33))
                        .debtPayment(BigDecimal.valueOf(33333.33))
                        .remainingDebt(BigDecimal.valueOf(0.01))
                        .build()))
            .build();

    assertEquals(creditDTO, creditDTOTest);
  }
}
