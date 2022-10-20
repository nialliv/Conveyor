package ru.artemev.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.service.DealService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Сделка")
@Validated
@RequiredArgsConstructor
public class DealController {

  private final DealService dealService;

  @PostMapping("/deal/application")
  @Operation(summary = "Расчёт возможных условий кредита")
  public ResponseEntity<List<LoanOfferDTO>> calculationPossibleLoans(
      @RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return ResponseEntity.ok(dealService.calculationPossibleLoans(loanApplicationRequestDTO));
  }

  @PutMapping("/deal/offer")
  @Operation(summary = "Выбор одного из предложений")
  public void selectOneOfOffers(@RequestBody @Valid LoanOfferDTO loanOfferDTO) {
    dealService.selectOneOfOffers(loanOfferDTO);
  }

  @PutMapping("/deal/calculate/{id}")
  @Operation(summary = "Завершение регистрации + полный подсчёт кредита")
  public void completionOfRegistration(
      @PathVariable Long id,
      @RequestBody @Valid FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
    dealService.completionOfRegistration(id, finishRegistrationRequestDTO);
  }

  @PutMapping("/deal/admin/{applicationId}/status")
  public void updateStatus(
      @PathVariable Long applicationId, @RequestBody ApplicationStatus applicationStatus) {
    dealService.updateApplicationStatus(applicationId, applicationStatus);
  }

  @PutMapping("/deal/document/{applicationId}/send")
  @Operation(summary = "Запрос на отправку документов")
  public void sendDocuments(@PathVariable Long applicationId) {
    dealService.sendDocuments(applicationId);
  }

  @PutMapping("/deal/document/{applicationId}/sign")
  @Operation(summary = "Запрос на подписание документов")
  public void signDocuments(@PathVariable Long applicationId) {
    dealService.signDocuments(applicationId);
  }

  @PutMapping("/deal/document/{applicationId}/code")
  @Operation(summary = "Подписание документов")
  public void codeDocuments(@PathVariable Long applicationId, @RequestBody Integer sesCode) {
    dealService.codeDocuments(applicationId, sesCode);
  }
}
