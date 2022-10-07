package ru.artemev.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;
import ru.artemev.application.exception.ValidationException;
import ru.artemev.application.service.ApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ApplicationServiceImplTest {

  @Autowired private ApplicationService applicationService;


  @Test
  void getLoanOfferDtoList() {

    // Check firstName
    checkValid(loanApplicationRequestDTOTestFirstName);
    // Check lastName
    checkValid(loanApplicationRequestDTOTestLastName);
    // Check middleName
    checkValid(loanApplicationRequestDTOTestMiddleName);
    // Check amount
    checkValid(loanApplicationRequestDTOTestAmount);
    // Check term
    checkValid(loanApplicationRequestDTOTestTerm);
    // Check birthday
    checkValid(loanApplicationRequestDTOTestBirthday);
    // Check email
    checkValid(loanApplicationRequestDTOTestEmail);
    // Check series
    checkValid(loanApplicationRequestDTOTestSeries);
    // Check number
    checkValid(loanApplicationRequestDTOTestNumber);

    // Check response
    assertEquals(
        loanOfferDTOList, applicationService.getLoanOfferDtoList(loanApplicationRequestDTO));
  }

  @Test
  void selectOneOfOffers() {
    LoanOfferDTO loanOfferDTO = applicationService.getLoanOfferDtoList(loanApplicationRequestDTO).get(0);

    applicationService.selectOneOfOffers(loanOfferDTO);


  }

  private void checkValid(LoanApplicationRequestDTO loanApplicationRequestDTO) {
    try {
      assertNotNull(applicationService.getLoanOfferDtoList(loanApplicationRequestDTO));
    } catch (ValidationException e) {
      log.info(e.getApiError() + " -> Received Exception");
      assertNotNull(e.getApiError());
    }
  }

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestFirstName =
      LoanApplicationRequestDTO.builder().firstName("t").build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestLastName =
      LoanApplicationRequestDTO.builder().firstName("test").lastName("t").build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestMiddleName =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("t")
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestAmount =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(1))
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestTerm =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(1)
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestBirthday =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(6)
          .birthday(LocalDate.of(2019, 1, 1))
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestEmail =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(6)
          .birthday(LocalDate.of(2000, 1, 1))
          .email("test")
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestSeries =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(6)
          .birthday(LocalDate.of(2000, 1, 1))
          .email("test@example.com")
          .passportSeries("123")
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTOTestNumber =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(6)
          .birthday(LocalDate.of(2000, 1, 1))
          .email("test@example.com")
          .passportSeries("1234")
          .passportNumber("1234")
          .build();

  private final LoanApplicationRequestDTO loanApplicationRequestDTO =
      LoanApplicationRequestDTO.builder()
          .firstName("test")
          .lastName("testov")
          .middleName("testovich")
          .amount(BigDecimal.valueOf(10_000))
          .term(6)
          .birthday(LocalDate.of(2000, 1, 1))
          .email("test@example.com")
          .passportSeries("1234")
          .passportNumber("123456")
          .build();

  private final List<LoanOfferDTO> loanOfferDTOList =
      List.of(
          LoanOfferDTO.builder()
              .applicationId(5L)
              .requestedAmount(BigDecimal.valueOf(10000))
              .totalAmount(BigDecimal.valueOf(10750))
              .term(6)
              .monthlyPayment(BigDecimal.valueOf(1791.67))
              .rate(BigDecimal.valueOf(15))
              .insuranceEnabled(false)
              .salaryClient(false)
              .build(),
          LoanOfferDTO.builder()
              .applicationId(5L)
              .requestedAmount(BigDecimal.valueOf(10000))
              .totalAmount(BigDecimal.valueOf(10600))
              .term(6)
              .monthlyPayment(BigDecimal.valueOf(1766.67))
              .rate(BigDecimal.valueOf(12))
              .insuranceEnabled(true)
              .salaryClient(false)
              .build(),
          LoanOfferDTO.builder()
              .applicationId(5L)
              .requestedAmount(BigDecimal.valueOf(10000))
              .totalAmount(BigDecimal.valueOf(10552))
              .term(6)
              .monthlyPayment(BigDecimal.valueOf(1758.67))
              .rate(BigDecimal.valueOf(11))
              .insuranceEnabled(false)
              .salaryClient(true)
              .build(),
          LoanOfferDTO.builder()
              .applicationId(5L)
              .requestedAmount(BigDecimal.valueOf(10000))
              .totalAmount(BigDecimal.valueOf(10402))
              .term(6)
              .monthlyPayment(BigDecimal.valueOf(1733.67))
              .rate(BigDecimal.valueOf(8))
              .insuranceEnabled(true)
              .salaryClient(true)
              .build());
}
