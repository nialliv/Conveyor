package ru.artemev.deal.service;

import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.model.enums.ApplicationStatus;

import java.util.List;

public interface DealService {
  List<LoanOfferDTO> calculationPossibleLoans(LoanApplicationRequestDTO loanApplicationRequestDTO);

  void selectOneOfOffers(LoanOfferDTO loanOfferDTO);

  void completionOfRegistration(Long id, FinishRegistrationRequestDTO finishRegistrationRequestDTO);

  void sendDocuments(Long applicationId);

  void signDocuments(Long applicationId);

  void codeDocuments(Long applicationId, Integer sesCode);

  void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus);
}
