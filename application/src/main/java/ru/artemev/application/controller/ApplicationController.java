package ru.artemev.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;
import ru.artemev.application.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/application")
@Tag(name = "Заявка")
public class ApplicationController {

  @Autowired private ApplicationService applicationService;

  @PostMapping()
  @Operation(summary = "Прескоринг + запрос на расчёт возможных условий кредита.")
  public ResponseEntity<List<LoanOfferDTO>> getLoanOfferDtoList(
      @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return ResponseEntity.ok(applicationService.getLoanOfferDtoList(loanApplicationRequestDTO));
  }

  @PutMapping("offer")
  @Operation(summary = "Выбор одного из предложений.")
  public void selectOneOfOffers(@RequestBody LoanOfferDTO loanOfferDTO) {
    applicationService.selectOneOfOffers(loanOfferDTO);
  }
}
