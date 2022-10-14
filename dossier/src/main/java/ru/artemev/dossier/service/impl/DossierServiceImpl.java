package ru.artemev.dossier.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.artemev.dossier.client.DealClient;
import ru.artemev.dossier.model.EmailMessage;
import ru.artemev.dossier.model.enums.ApplicationStatus;
import ru.artemev.dossier.service.DossierService;

@Service
@Slf4j
public class DossierServiceImpl implements DossierService {

  @Autowired private DealClient dealClient;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @KafkaListener(topics = {"conveyor-send-documents"})
  public void sendDocuments(String message) throws JsonProcessingException {
    log.info("====== Started sendDocuments =======");
    log.info("Received String in Dossier => " + message);

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    dealClient.updateStatus(
        emailMessageEntity.getApplicationId(), ApplicationStatus.DOCUMENT_CREATED);

    /*
    TODO: добавить отправку email
     */

    log.info("====== Finished sendDocuments =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-sign-documents"})
  public void signDocuments(String message) throws JsonProcessingException {
    log.info("====== Started signDocuments =======");

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    /*
    TODO: добавить отправку email
     */

    log.info("====== Finished signDocuments =======");
  }

  @Override
  @KafkaListener(topics = {"conveyor-credit"})
  public void codeDocuments(String message) throws JsonProcessingException {
    log.info("====== Started codeDocuments =======");

    EmailMessage emailMessageEntity = objectMapper.readValue(message, EmailMessage.class);
    log.info("Mapping result => " + emailMessageEntity.toString());

    /*
    TODO: добавить отправку email
     */

    log.info("====== Finished codeDocuments =======");
  }
}
