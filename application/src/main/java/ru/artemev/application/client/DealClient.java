package ru.artemev.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "deal-client", url = "${deal.client.url}")
public interface DealClient {
  @PostMapping("/deal/application")
  List<LoanOfferDTO> calculationPossibleLoans(
      @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

  @PutMapping("/deal/offer")
  void selectOneOfOffers(@RequestBody LoanOfferDTO loanOfferDTO);
}
