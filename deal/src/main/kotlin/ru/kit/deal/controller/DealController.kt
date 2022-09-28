package ru.kit.deal.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kit.deal.dto.FinishRegistrationRequestDTO
import ru.kit.deal.dto.LoanApplicationRequestDTO
import ru.kit.deal.dto.LoanOfferDTO
import ru.kit.deal.service.DealService

@RestController
@RequestMapping("/api")
@Tag(name = "Сделка")
class DealController(
    private val dealService: DealService,
) {
    @PostMapping("/deal/application")
    @Operation(summary = "Расчёт возможных условий кредита")
    fun calculationPossibleLoans(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        return ResponseEntity.ok(dealService.calculationPossibleLoans(loanApplicationRequestDTO))
    }

    @PutMapping("/deal/offer")
    @Operation(summary = "Выбор одного из предложений")
    fun selectOneOfOffers(@RequestBody loanOfferDTO: LoanOfferDTO) {
        dealService.selectOneOfOffers(loanOfferDTO)
    }

    @PutMapping("/deal/calculate/{id}")
    @Operation(summary = "Завершение регистрации + полный подсчёт кредита")
    fun completionOfRegistration(
        @PathVariable id: Long,
        @RequestBody finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    ) {
        dealService.completionOfRegistration(id, finishRegistrationRequestDTO)
    }
}