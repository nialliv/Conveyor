package ru.artemev.conveyor.service;

import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
  List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

  CreditDTO getCreditDto(ScoringDataDTO scoringDataDTO);
}
