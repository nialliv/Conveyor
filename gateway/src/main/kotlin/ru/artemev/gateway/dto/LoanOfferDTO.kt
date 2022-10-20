package ru.artemev.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Предложение по кредиту")
data class LoanOfferDTO(
    @Schema(description = "Итоговая сумма", example = "15000")
    @JsonProperty("totalAmount")
    val totalAmount: BigDecimal? = null,

    @Schema(description = "Срок займа", example = "10")
    @JsonProperty("term")
    val term: Int? = null,

    @Schema(description = "Ежемесячный платеж", example = "1500")
    @JsonProperty("monthlyPayment")
    val monthlyPayment: BigDecimal? = null,

    @Schema(description = "Процентная ставка", example = "10")
    @JsonProperty("rate")
    val rate: BigDecimal? = null,

    @Schema(description = "Страховка", example = "true")
    @JsonProperty("isInsuranceEnabled")
    val isInsuranceEnabled: Boolean = false,

    @Schema(description = "Зарплатный клиент", example = "true")
    @JsonProperty("isSalaryClient")
    val isSalaryClient: Boolean = false,

    @Schema(description = "Номер предложения", example = "4")
    @JsonProperty("applicationId")
    var applicationId: Long? = null,

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    @JsonProperty("requestedAmount")
    val requestedAmount: BigDecimal? = null
)
