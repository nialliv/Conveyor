package ru.artemev.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import ru.artemev.deal.client.ConveyorClient;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.entity.CreditEntity;
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
import ru.artemev.deal.service.impl.DealServiceImpl;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class DealServiceImplTest {

  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final LoanApplicationRequestDTO loanApplicationRequestDTO =
      mapper.readValue(
          new File("src/test/resources/json/LoanApplicationRequestDTO.json"),
          LoanApplicationRequestDTO.class);
  private final List<LoanOfferDTO> loanOfferDTOList =
      Arrays.asList(
          mapper.readValue(
              new File("src/test/resources/json/LoanOfferDTOList.json"), LoanOfferDTO[].class));
  @InjectMocks private DealServiceImpl dealService;
  @Mock private ClientRepository clientRepository;
  @Mock private ApplicationRepository applicationRepository;
  @Mock private CreditRepository creditRepository;
  @Mock private ClientEntityMapper clientEntityMapper;
  @Mock private CreditEntityMapper creditEntityMapper;
  @Mock private ScoringDataDTOMapper scoringDataDTOMapper;
  @Mock private ConveyorClient conveyorClient;
  @Mock private KafkaTemplate<Long, EmailMessage> kafkaTemplate;

  DealServiceImplTest() throws IOException {}

  @Test
  @DisplayName("Testing calculationPossibleLoans")
  void calculationPossibleLoans() {
    ClientEntity clientEntity = clientEntityMapper.toClientEntity(loanApplicationRequestDTO);
    ApplicationEntity applicationEntity =
        ApplicationEntity.builder()
            .clientEntity(clientEntity)
            .creationDate(LocalDate.now())
            .build();

    loanOfferDTOList.forEach(
        el -> el.setTotalAmount(el.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN)));

    when(clientRepository.save(any())).thenReturn(clientEntity);
    when(applicationRepository.save(any())).thenReturn(applicationEntity);
    when(conveyorClient.getOffers(loanApplicationRequestDTO)).thenReturn(loanOfferDTOList);
    when(clientEntityMapper.toClientEntity(loanApplicationRequestDTO)).thenReturn(clientEntity);

    assertEquals(
        loanOfferDTOList,
        dealService.calculationPossibleLoans(loanApplicationRequestDTO).stream()
            .peek(el -> el.setApplicationId(1L))
            .collect(Collectors.toList()));
  }

  @Test
  @DisplayName("Testing selectOneOfOffers")
  void selectOneOfOffers() {
    ClientEntity clientEntity = ClientEntity.builder().build();
    ApplicationEntity applicationEntity = ApplicationEntity.builder().id(1L).clientEntity(clientEntity).build();
    when(applicationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(applicationEntity));
    when(clientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(clientEntity));

    dealService.selectOneOfOffers(loanOfferDTOList.get(0));

    List<ApplicationHistory> applicationHistoryList = applicationEntity.getStatusHistory();
    LoanOfferDTO appliedOffer = applicationEntity.getAppliedOffer();

    assertNotNull(applicationHistoryList);
    assertEquals(applicationHistoryList.get(0).getStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(applicationHistoryList.get(0).getDate(), LocalDate.now());
    assertEquals(applicationEntity.getApplicationStatus(), ApplicationStatus.PREAPPROVAL);
    assertEquals(appliedOffer, loanOfferDTOList.get(0));
    verify(kafkaTemplate, VerificationModeFactory.times(1))
        .send(
            "conveyor-finish-registration",
            applicationEntity.getId(),
            new EmailMessage(clientEntity.getEmail(), Theme.FINISH_REGISTRATION, applicationEntity.getId()));
  }

  @Test
  @DisplayName("Testing completionOfRegistration")
  void completionOfRegistration() throws IOException {
    ClientEntity clientEntity = ClientEntity.builder().email("test@example.com").build();
    ApplicationEntity applicationEntity =
        ApplicationEntity.builder()
            .id(1L)
            .statusHistory(new ArrayList<>())
            .clientEntity(clientEntity)
            .creditEntity(CreditEntity.builder().build())
            .build();

    FinishRegistrationRequestDTO finishRegistrationRequestDTO =
        mapper.readValue(
            new File("src/test/resources/json/FinishRegistrationRequestDTO.json"),
            FinishRegistrationRequestDTO.class);

    CreditDTO creditDTO = mapper.readValue(new File("src/test/resources/json/CreditDTO.json"), CreditDTO.class);
    CreditEntity creditEntity = applicationEntity.getCreditEntity();

    when(applicationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(applicationEntity));
    when(conveyorClient.getCreditDto(any())).thenReturn(creditDTO);
    when(creditEntityMapper.toCreditEntity(creditDTO)).thenReturn(creditEntity);

    dealService.completionOfRegistration(applicationEntity.getId(), finishRegistrationRequestDTO);

    assertNotNull(creditEntity);

    assertEquals(creditEntity.getCreditStatus(), CreditStatus.CALCULATED);
    assertEquals(applicationEntity.getApplicationStatus(), ApplicationStatus.CC_APPROVED);

    verify(kafkaTemplate, VerificationModeFactory.times(1))
        .send(
            "conveyor-create-documents",
            applicationEntity.getId(),
            new EmailMessage(
                clientEntity.getEmail(), Theme.CREATE_DOCUMENTS, applicationEntity.getId()));
  }
}
