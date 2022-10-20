package ru.artemev.gateway.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.artemev.gateway.client.ApplicationClient
import ru.artemev.gateway.client.DealClient
import ru.artemev.gateway.dto.FinishRegistrationRequestDTO
import ru.artemev.gateway.dto.LoanApplicationRequestDTO
import ru.artemev.gateway.dto.LoanOfferDTO

@RestController
@Tag(name = "Gateway")
class GatewayController(
    private val applicationClient: ApplicationClient,
    private val dealClient: DealClient
) {
    val logger: Logger = LogManager.getLogger()


    @PostMapping("/application")
    @Operation(summary = "Прескоринг + запрос на расчёт возможных условий кредита.")
    fun getLoanOfferDtoList(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        logger.info("Received  -> $loanApplicationRequestDTO")
        return applicationClient.getLoanOfferDtoList(loanApplicationRequestDTO)
    }

    @PostMapping("/application/apply")
    @Operation(summary = "Выбор одного из предложений.")
    fun selectOneOfOffers(@RequestBody loanOfferDTO: LoanOfferDTO) {
        logger.info("Received  -> $loanOfferDTO")
        applicationClient.selectOneOfOffers(loanOfferDTO)
    }

    @PostMapping("/application/registration/{applicationId}")
    @Operation(summary = "Завершение регистрации + полный подсчёт кредита")
    fun completionOfRegistration(
        @PathVariable applicationId: Long,
        @RequestBody finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    ) {
        logger.info("Received  -> $finishRegistrationRequestDTO")
        dealClient.completionOfRegistration(applicationId, finishRegistrationRequestDTO)
    }

    @PostMapping("document/{applicationId}")
    @Operation(summary = "Запрос на отправку документов")
    fun sendDocuments(@PathVariable applicationId: Long) {
        logger.info("Received ApplicationId -> $applicationId")
        dealClient.sendDocuments(applicationId)
    }

    @PostMapping("document/{applicationId}/sign")
    @Operation(summary = "Запрос на подписание документов")
    fun signDocuments(@PathVariable applicationId: Long) {
        logger.info("Received ApplicationId -> $applicationId")
        dealClient.signDocuments(applicationId)
    }

    @PostMapping("document/{applicationId}/sign/code")
    @Operation(summary = "Подписание документов")
    fun codeDocuments(
        @PathVariable applicationId: Long,
        @RequestBody sesCode: Int
    ) {
        logger.info("Received ApplicationId -> $applicationId")
        dealClient.codeDocuments(applicationId, sesCode)
    }
}