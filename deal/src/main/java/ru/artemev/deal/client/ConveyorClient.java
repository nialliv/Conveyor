package ru.artemev.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyor-client", url = "${conveyor.client.url}")
public interface ConveyorClient {
  @PostMapping("/conveyor/offers")
  List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

  @PostMapping("/conveyor/calculation")
  CreditDTO getCreditDto(@RequestBody ScoringDataDTO scoringDataDTO);
}
