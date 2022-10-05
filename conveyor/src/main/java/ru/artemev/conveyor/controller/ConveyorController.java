package ru.artemev.conveyor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.service.ConveyorService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "кредитный конвейер")
@Validated
public class ConveyorController {

  @Autowired private ConveyorService conveyorService;

  @PostMapping("/conveyor/offers")
  @Operation(summary = "Получение списка LoanOfferDTO")
  public List<LoanOfferDTO> getOffers(
      @RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      throw new ValidationException(bindingResult.getModel().toString());
    return conveyorService.getOffers(loanApplicationRequestDTO);
  }

  @PostMapping("/conveyor/calculation")
  @Operation(
      summary = "Валидация присланных данных + скоринг данных + полный расчет параметров кредита")
  public CreditDTO getCreditDto(
      @RequestBody @Valid ScoringDataDTO scoringDataDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      throw new ValidationException(bindingResult.getModel().toString());
    return conveyorService.getCreditDto(scoringDataDTO);
  }
}
