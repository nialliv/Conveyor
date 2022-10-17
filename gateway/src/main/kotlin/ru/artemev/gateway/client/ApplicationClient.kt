package ru.artemev.gateway.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.artemev.gateway.dto.LoanApplicationRequestDTO
import ru.artemev.gateway.dto.LoanOfferDTO

@FeignClient(name = "application-client", url = "\${application.client.url}")
interface ApplicationClient {

    @PostMapping("/application")
    fun getLoanOfferDtoList(
        @RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO
    ): List<LoanOfferDTO>

    @PutMapping("/application/offer")
    fun selectOneOfOffers(
        @RequestBody loanOfferDTO: LoanOfferDTO
    )
}