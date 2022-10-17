package ru.artemev.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artemev.application.controller.ApplicationController;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;
import ru.artemev.application.exception.ValidationException;
import ru.artemev.application.service.ApplicationService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ApplicationServiceImplTest {

  @Autowired private ApplicationService applicationService;
  @Autowired private ApplicationController applicationController;

  ApplicationServiceImplTest() throws IOException {}

  @Test
  @DisplayName("Testing getLoanOfferDtoList")
  void getLoanOfferDtoList() throws IOException {

    // Check firstName
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestFirstName.json"),
            LoanApplicationRequestDTO.class));
    // Check lastName
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestLastName.json"),
            LoanApplicationRequestDTO.class));
    // Check middleName
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestMiddleName.json"),
            LoanApplicationRequestDTO.class));
    // Check amount
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestAmount.json"),
            LoanApplicationRequestDTO.class));
    // Check term
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestTerm.json"),
            LoanApplicationRequestDTO.class));
    // Check birthday
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestBirthday.json"),
            LoanApplicationRequestDTO.class));
    // Check email
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestEmail.json"),
            LoanApplicationRequestDTO.class));
    // Check series
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestPassportSeries.json"),
            LoanApplicationRequestDTO.class));
    // Check number
    checkValid(
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTOTestPassportNumber.json"),
            LoanApplicationRequestDTO.class));

    List<LoanOfferDTO> loanOfferDTOList =
        Arrays.asList(
            mapper.readValue(
                new File("src/test/resources/json/LoanOfferDTOList.json"), LoanOfferDTO[].class));
    log.info(loanOfferDTOList.toString());
    // Check response
    assertEquals(
        loanOfferDTOList, applicationService.getLoanOfferDtoList(loanApplicationRequestDTO));
  }

  @Test
  @DisplayName("Testing selectOneOfOffers")
  void selectOneOfOffers() {
    LoanOfferDTO loanOfferDTO =
        applicationService.getLoanOfferDtoList(loanApplicationRequestDTO).get(0);

    applicationService.selectOneOfOffers(loanOfferDTO);
  }

  private void checkValid(LoanApplicationRequestDTO loanApplicationRequestDTO) {
    try {
      assertNotNull(applicationController.getLoanOfferDtoList(loanApplicationRequestDTO));
    } catch (javax.validation.ConstraintViolationException e) {
      assertNotNull(e);
      log.info(e.getMessage() + " -> Received Exception");
    } catch (ValidationException e) {
      assertNotNull(e.getApiError());
      log.info(e.getApiError() + " -> Received Exception");
    }
  }

  private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

  private final LoanApplicationRequestDTO loanApplicationRequestDTO =
      mapper.readValue(
          new File("src/test/resources/json/LoanApplicationRequestDTO.json"),
          LoanApplicationRequestDTO.class);
}
