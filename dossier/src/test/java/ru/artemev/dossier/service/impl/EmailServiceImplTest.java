package ru.artemev.dossier.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.artemev.dossier.service.EmailService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EmailServiceImplTest {

  @Autowired private EmailService emailService;

  @MockBean private JavaMailSender javaMailSender;

  @Test
  @DisplayName("Testing sendSimpleMessage")
  void sendSimpleMessage() {

    emailService.sendSimpleMessage("example@ya.ru", "TEST", "Test Message");

    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }
}
