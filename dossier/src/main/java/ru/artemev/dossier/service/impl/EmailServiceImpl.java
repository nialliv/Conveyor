package ru.artemev.dossier.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.artemev.dossier.service.EmailService;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  @Autowired private JavaMailSender javaMailSender;

  @Override
  public void sendSimpleMessage(String address, String subject, String message) {
    log.info("====== Started sendSimpleMessage =======");

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    simpleMailMessage.setTo(address);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);

    javaMailSender.send(simpleMailMessage);

    log.info("====== Finished sendSimpleMessage =======");
  }
}
