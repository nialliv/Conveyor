package ru.artemev.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "deal-client", url = "localhost:8081/api/deal")
public interface DealClient {
  @PostMapping("application")
  List<LoanOfferDTO> calculationPossibleLoans(
      @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

  @PutMapping("offer")
  void selectOneOfOffers(@RequestBody LoanOfferDTO loanOfferDTO);
}
