package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDate

@Data
@Builder
@Schema(description = "Предложение по кредиту")
class LoanOfferDTO {
    @Schema(description = "Номер предложения", example = "4")
    private val applicationId: Long? = null

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private val requestedAmount: BigDecimal? = null

    @Schema(description = "Итоговая сумма", example = "15000")
    private val totalAmount: BigDecimal? = null

    @Schema(description = "Срок займа", example = "10")
    private val term: Int? = null

    @Schema(description = "Ежемесячный платеж", example = "1500")
    private val monthlyPayment: BigDecimal? = null

    @Schema(description = "Процентная ставка", example = "10")
    private val rate: BigDecimal? = null

    @Schema(description = "Страховка", example = "true")
    private val isInsuranceEnabled = false

    @Schema(description = "Зарплатный клиент", example = "true")
    private val isSalaryClient = false
}