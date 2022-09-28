package ru.kit.deal

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.kit.deal.dto.LoanApplicationRequestDTO
import ru.kit.deal.dto.LoanOfferDTO

@FeignClient(name = "conveyor-client", url = "localhost:8080/api")
interface ConveyorClient {
    @PostMapping("/conveyor/offers")
    fun getOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>
}