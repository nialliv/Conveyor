package ru.artemev.conveyor.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.model.PaymentScheduleElement;
import ru.artemev.conveyor.service.ConveyorService;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ConveyorServiceImpTest {

  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired private ConveyorService conveyorService;

  @Test
  @DisplayName("Testing getOffers")
  void getOffers() throws IOException {
    LoanApplicationRequestDTO loanApplicationRequestDTO =
        mapper.readValue(
            new File("src/test/resources/json/LoanApplicationRequestDTO.json"),
            LoanApplicationRequestDTO.class);

    List<LoanOfferDTO> loanOfferDTOList = conveyorService.getOffers(loanApplicationRequestDTO);

    List<LoanOfferDTO> loanOfferDTOListTest =
        Arrays.asList(
            mapper.readValue(
                new File("src/test/resources/json/LoanOfferDTOList.json"), LoanOfferDTO[].class));
    loanOfferDTOListTest.forEach(
        a -> a.setTotalAmount(a.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN)));

    assertEquals(loanOfferDTOList, loanOfferDTOListTest);
  }

  @Test
  @DisplayName("Testing getCreditDto")
  void getCreditDto() throws IOException {

    ScoringDataDTO scoringDataDTO =
        mapper.readValue(
            new File("src/test/resources/json/ScoringDataDTO.json"), ScoringDataDTO.class);

    CreditDTO creditDTO = conveyorService.getCreditDto(scoringDataDTO);

    CreditDTO creditDTOTest =
        mapper.readValue(new File("src/test/resources/json/CreditDTO.json"), CreditDTO.class);
    creditDTOTest.setPsk(creditDTOTest.getPsk().setScale(2, RoundingMode.HALF_EVEN));

    List<PaymentScheduleElement> paymentScheduleElementList = creditDTO.getPaymentSchedule();

    paymentScheduleElementList.forEach(
        el -> {
          el.setTotalPayment(el.getTotalPayment().setScale(2, RoundingMode.HALF_EVEN));
          el.setRemainingDebt(el.getRemainingDebt().setScale(2, RoundingMode.HALF_EVEN));
        });

    creditDTOTest.setPaymentSchedule(paymentScheduleElementList);

    assertEquals(creditDTO, creditDTOTest);
  }
}
