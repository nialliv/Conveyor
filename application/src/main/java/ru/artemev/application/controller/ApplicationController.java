package ru.artemev.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;
import ru.artemev.application.service.ApplicationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/application")
@Tag(name = "Заявка")
@Validated
public class ApplicationController {

  @Autowired private ApplicationService applicationService;

  @PostMapping()
  @Operation(summary = "Прескоринг + запрос на расчёт возможных условий кредита.")
  public List<LoanOfferDTO> getLoanOfferDtoList(
      @RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return applicationService.getLoanOfferDtoList(loanApplicationRequestDTO);
  }

  @PutMapping("offer")
  @Operation(summary = "Выбор одного из предложений.")
  public void selectOneOfOffers(@RequestBody @Valid LoanOfferDTO loanOfferDTO) {
    applicationService.selectOneOfOffers(loanOfferDTO);
  }
}
