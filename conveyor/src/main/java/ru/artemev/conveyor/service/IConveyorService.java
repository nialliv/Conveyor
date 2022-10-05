package ru.artemev.conveyor.service;

import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;

import java.util.List;

<<<<<<< HEAD:conveyor/src/main/java/ru/artemev/conveyor/service/IConveyorService.java
public interface IConveyorService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);
=======
public interface ConveyorService {
  List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);
>>>>>>> f5a3fee (reformatted code):conveyor/src/main/java/ru/artemev/conveyor/service/ConveyorService.java

  CreditDTO getCreditDto(ScoringDataDTO scoringDataDTO);
}
