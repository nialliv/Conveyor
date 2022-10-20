package ru.artemev.dossier.service;

public interface EmailService {
  void sendSimpleMessage(String address, String subject, String message);
}
