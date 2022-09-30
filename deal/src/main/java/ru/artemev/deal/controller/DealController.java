package ru.artemev.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Сделка")
public class DealController {

  @Autowired private DealService dealService;

  @PostMapping("/deal/application")
  @Operation(summary = "Расчёт возможных условий кредита")
  public ResponseEntity<List<LoanOfferDTO>> calculationPossibleLoans(
      @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return ResponseEntity.ok(dealService.calculationPossibleLoans(loanApplicationRequestDTO));
  }

  @PutMapping("/deal/offer")
  @Operation(summary = "Выбор одного из предложений")
  public void selectOneOfOffers(@RequestBody LoanOfferDTO loanOfferDTO) {
    dealService.selectOneOfOffers(loanOfferDTO);
  }

  @PutMapping("/deal/calculate/{id}")
  @Operation(summary = "Завершение регистрации + полный подсчёт кредита")
  public void completionOfRegistration(
      @PathVariable Long id,
      @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
    dealService.completionOfRegistration(id, finishRegistrationRequestDTO);
  }
}
