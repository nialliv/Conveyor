package ru.artemev.application.service;

import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    List<LoanOfferDTO> getLoanOfferDtoList(LoanApplicationRequestDTO loanApplicationRequestDTO);
    void selectOneOfOffers(LoanOfferDTO loanOfferDTO);
}
