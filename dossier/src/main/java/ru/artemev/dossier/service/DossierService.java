package ru.artemev.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DossierService {
  void sendDocuments(String message) throws JsonProcessingException;

  void signDocuments(String message) throws JsonProcessingException;

  void codeDocuments(String message) throws JsonProcessingException;
}
