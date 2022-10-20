package ru.artemev.gateway.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.artemev.gateway.dto.FinishRegistrationRequestDTO

@FeignClient(name = "deal-client", url = "\${deal.client.url}")
interface DealClient {

    @PutMapping("/deal/calculate/{applicationId}")
    fun completionOfRegistration(
        @PathVariable applicationId: Long,
        finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    )

    @PutMapping("/deal/document/{applicationId}/send")
    fun sendDocuments(@PathVariable applicationId: Long)

    @PutMapping("/deal/document/{applicationId}/sign")
    fun signDocuments(@PathVariable applicationId: Long)

    @PutMapping("/deal/document/{applicationId}/code")
    fun codeDocuments(@PathVariable applicationId: Long, @RequestBody sesCode: Int)
}