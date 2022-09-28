package ru.kit.deal.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kit.deal.dto.FinishRegistrationRequestDTO
import ru.kit.deal.dto.LoanApplicationRequestDTO
import ru.kit.deal.dto.LoanOfferDTO
import ru.kit.deal.service.DealService

@RestController
@RequestMapping("deal")
class DealController(
    private val dealService: DealService,
) {
    @PostMapping("application")
    fun calculationPossibleLoans(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        return ResponseEntity.ok(dealService.calculationPossibleLoans(loanApplicationRequestDTO))
    }

    @PutMapping("offer")
    fun selectOneOfOffers(@RequestBody loanOfferDTO: LoanOfferDTO) {
        dealService.selectOneOfOffers(loanOfferDTO)
    }

    @PutMapping("calculate/{id}")
    fun completionOfRegistration(
        @PathVariable id: Long,
        @RequestBody finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    ) {
        dealService.completionOfRegistration(id, finishRegistrationRequestDTO)
    }
}