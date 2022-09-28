package ru.kit.deal.service

import ru.kit.deal.dto.FinishRegistrationRequestDTO
import ru.kit.deal.dto.LoanApplicationRequestDTO
import ru.kit.deal.dto.LoanOfferDTO

interface DealService {
    fun calculationPossibleLoans(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>
    fun selectOneOfOffers(loanOfferDTO: LoanOfferDTO): Unit
    fun completionOfRegistration(id: Long, finishRegistrationRequestDTO: FinishRegistrationRequestDTO)
}