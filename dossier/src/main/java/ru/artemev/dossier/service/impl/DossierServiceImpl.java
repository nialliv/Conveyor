package ru.artemev.dossier.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.artemev.dossier.client.DealClient;
import ru.artemev.dossier.model.EmailMessage;
import ru.artemev.dossier.model.enums.ApplicationStatus;
import ru.artemev.dossier.service.DossierService;
import ru.artemev.dossier.service.EmailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {

  private final DealClient dealClient;

  private final EmailService emailService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @KafkaListener(topics = {"conveyor-finish-registration"})
  public void finishRegistration(String message) throws JsonProcessingException {
    log.info("====== Started finishRegistration =======");
    log.info("Received String in Dossier => " + message);

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    emailService.sendSimpleMessage(
        emailMessageEntity.getAddress(),
        emailMessageEntity.getTheme().toString(),
        "Finish registration");

    log.info("====== Finished finishRegistration =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-create-documents"})
  public void createDocuments(String message) throws JsonProcessingException {
    log.info("====== Started createDocuments =======");
    log.info("Received String in Dossier => " + message);

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    emailService.sendSimpleMessage(
        emailMessageEntity.getAddress(),
        emailMessageEntity.getTheme().toString(),
        "Create document");

    log.info("====== Finished createDocuments =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-send-documents"})
  public void sendDocuments(String message) throws JsonProcessingException {
    log.info("====== Started sendDocuments =======");
    log.info("Received String in Dossier => " + message);

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    dealClient.updateStatus(
        emailMessageEntity.getApplicationId(), ApplicationStatus.DOCUMENT_CREATED);

    emailService.sendSimpleMessage(
        emailMessageEntity.getAddress(),
        emailMessageEntity.getTheme().toString(),
        "Your loan documents");

    log.info("====== Finished sendDocuments =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-sign-documents"})
  public void signDocuments(String message) throws JsonProcessingException {
    log.info("====== Started signDocuments =======");

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    emailService.sendSimpleMessage(
        emailMessageEntity.getAddress(),
        emailMessageEntity.getTheme().toString(),
        "Sign documents with SES code");

    log.info("====== Finished signDocuments =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-credit"})
  public void codeDocuments(String message) throws JsonProcessingException {
    log.info("====== Started codeDocuments =======");

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    emailService.sendSimpleMessage(
        emailMessageEntity.getAddress(), emailMessageEntity.getTheme().toString(), "Credit issued");

    log.info("====== Finished codeDocuments =======");
  }
}
