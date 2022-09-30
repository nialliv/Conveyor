package ru.artemev.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyor-client", url = "localhost:8080/api")
public interface ConveyorClient {
    @PostMapping("/conveyor/offers")
    ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/conveyor/calculation")
    ResponseEntity<CreditDTO> getCreditDto(@RequestBody ScoringDataDTO scoringDataDTO);
}

