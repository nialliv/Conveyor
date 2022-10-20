package ru.artemev.dossier.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.artemev.dossier.client.DealClient;
import ru.artemev.dossier.model.enums.ApplicationStatus;
import ru.artemev.dossier.model.enums.Theme;
import ru.artemev.dossier.service.DossierService;
import ru.artemev.dossier.service.EmailService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DossierServiceImplTest {

  @Autowired private DossierService dossierService;

  @MockBean private EmailService emailService;

  @MockBean private DealClient dealClient;

  @Test
  @DisplayName("Testing finishRegistration")
  void finishRegistration() {
    try {
      dossierService.finishRegistration(
          Files.readString(Path.of("src/test/resources/json/EmailMessage.json")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    verify(emailService, times(1))
        .sendSimpleMessage(anyString(), eq(Theme.CREATE_DOCUMENTS.toString()), anyString());
  }

  @Test
  @DisplayName("Testing createDocuments")
  void createDocuments() {
    try {
      dossierService.createDocuments(
          Files.readString(Path.of("src/test/resources/json/EmailMessage.json")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    verify(emailService, times(1))
        .sendSimpleMessage(anyString(), eq(Theme.CREATE_DOCUMENTS.toString()), anyString());
  }

  @Test
  @DisplayName("Testing sendDocuments")
  void sendDocuments() {
    try {
      dossierService.sendDocuments(
          Files.readString(Path.of("src/test/resources/json/EmailMessage.json")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    verify(emailService, times(1))
        .sendSimpleMessage(anyString(), eq(Theme.CREATE_DOCUMENTS.toString()), anyString());
    verify(dealClient, times(1)).updateStatus(anyLong(), any(ApplicationStatus.class));
  }

  @Test
  @DisplayName("Testing signDocuments")
  void signDocuments() {
    try {
      dossierService.signDocuments(
          Files.readString(Path.of("src/test/resources/json/EmailMessage.json")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    verify(emailService, times(1))
        .sendSimpleMessage(anyString(), eq(Theme.CREATE_DOCUMENTS.toString()), anyString());
  }

  @Test
  @DisplayName("Testing codeDocuments")
  void codeDocuments() {
    try {
      dossierService.codeDocuments(
          Files.readString(Path.of("src/test/resources/json/EmailMessage.json")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    verify(emailService, times(1))
        .sendSimpleMessage(anyString(), eq(Theme.CREATE_DOCUMENTS.toString()), anyString());
  }
}
