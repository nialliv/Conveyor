package ru.artemev.gateway.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal


@Schema(description = "Предложение по кредиту")
data class LoanOfferDTO(
    @Schema(description = "Итоговая сумма", example = "15000")
    val totalAmount: BigDecimal? = null,

    @Schema(description = "Срок займа", example = "10")
    val term: Int? = null,

    @Schema(description = "Ежемесячный платеж", example = "1500")
    val monthlyPayment: BigDecimal? = null,

    @Schema(description = "Процентная ставка", example = "10")
    val rate: BigDecimal? = null,

    @Schema(description = "Страховка", example = "true")
    val insuranceEnabled: Boolean = false,

    @Schema(description = "Зарплатный клиент", example = "true")
    val salaryClient: Boolean = false,

    @Schema(description = "Номер предложения", example = "4")
    val applicationId: Long? = null,

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    val requestedAmount: BigDecimal? = null
)