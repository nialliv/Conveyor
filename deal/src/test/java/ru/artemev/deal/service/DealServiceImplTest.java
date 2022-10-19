package ru.artemev.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.entity.CreditEntity;
import ru.artemev.deal.mapper.ClientEntityMapper;
import ru.artemev.deal.mapper.CreditEntityMapper;
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.EmailMessage;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.model.enums.CreditStatus;
import ru.artemev.deal.model.enums.Theme;
import ru.artemev.deal.repository.ApplicationRepository;
import ru.artemev.deal.repository.ClientRepository;
import ru.artemev.deal.repository.CreditRepository;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class DealServiceImplTest {

  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final LoanApplicationRequestDTO loanApplicationRequestDTO =
      mapper.readValue(
          new File("src/test/resources/json/LoanApplicationRequestDTO.json"),
          LoanApplicationRequestDTO.class);
  @Autowired private DealService dealService;
  @Autowired private ClientRepository clientRepository;
  @Autowired private ApplicationRepository applicationRepository;
  @Autowired private CreditRepository creditRepository;
  @Autowired private ClientEntityMapper clientEntityMapper;
  @Autowired private CreditEntityMapper creditEntityMapper;
  @MockBean private KafkaTemplate<Long, EmailMessage> kafkaTemplate;

  DealServiceImplTest() throws IOException {}

  @Test
  @DisplayName("Testing calculationPossibleLoans")
  void calculationPossibleLoans() throws IOException {

    ClientEntity clientEntity = clientEntityMapper.toClientEntity(loanApplicationRequestDTO);
    ApplicationEntity applicationEntity =
        ApplicationEntity.builder()
            .clientEntity(clientEntity)
            .creationDate(LocalDate.now())
            .build();

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
        Arrays.asList(
            mapper.readValue(
                new File("src/test/resources/json/LoanOfferDTOList.json"), LoanOfferDTO[].class));

    loanOfferDTOList.forEach(
        el -> el.setTotalAmount(el.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN)));

    assertEquals(
        loanOfferDTOList,
        dealService.calculationPossibleLoans(loanApplicationRequestDTO).stream()
            .peek(el -> el.setApplicationId(1L))
            .collect(Collectors.toList()));
  }

  @Test
  @DisplayName("Testing selectOneOfOffers")
  void selectOneOfOffers() {

    List<LoanOfferDTO> loanOfferDTOList =
        dealService.calculationPossibleLoans(loanApplicationRequestDTO);

    assertNotNull(loanOfferDTOList);

    dealService.selectOneOfOffers(loanOfferDTOList.get(0));

    ApplicationEntity applicationEntity =
        applicationRepository.findById(loanOfferDTOList.get(0).getApplicationId()).get();

    assertNotNull(applicationEntity);

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();

    // fix scale error
    LoanOfferDTO appliedOffer = applicationEntity.getAppliedOffer();
    appliedOffer.setTotalAmount(appliedOffer.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN));

    assertNotNull(applicationHistoryList);
    assertEquals(applicationHistoryList.get(0).getStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(applicationHistoryList.get(0).getDate(), LocalDate.now());
    assertEquals(applicationEntity.getApplicationStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(appliedOffer, loanOfferDTOList.get(0));
    verify(kafkaTemplate, VerificationModeFactory.times(1))
        .send(
            "conveyor-finish-registration",
            1L,
            new EmailMessage("test@example.com", Theme.FINISH_REGISTRATION, 1L));
  }

  @Test
  @DisplayName("Testing completionOfRegistration")
  void completionOfRegistration() throws IOException {

    FinishRegistrationRequestDTO finishRegistrationRequestDTO =
        mapper.readValue(
            new File("src/test/resources/json/FinishRegistrationRequestDTO.json"),
            FinishRegistrationRequestDTO.class);

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

    verify(kafkaTemplate, VerificationModeFactory.times(1))
        .send(
            "conveyor-finish-registration",
            1L,
            new EmailMessage("test@example.com", Theme.FINISH_REGISTRATION, 1L));
  }
}
