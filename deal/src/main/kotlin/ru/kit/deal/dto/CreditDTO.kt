package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Data
import java.math.BigDecimal


@Schema(description = "Кредит")
data class CreditDTO (
    @Schema(description = "Сумма", example = "10000")
    private val amount: BigDecimal? = null,

    @Schema(description = "Срок займа", example = "6")
    private val term: Int? = null,

    @Schema(description = "Ежемесячный платеж", example = "1500")
    private val monthlyPayment: BigDecimal? = null,

    @Schema(description = "Процент кредита", example = "15")
    private val rate: BigDecimal? = null,

    @Schema(description = "Полная стоимость кредита", example = "15")
    private val psk: BigDecimal? = null,

    @Schema(description = "Страховка", example = "true")
    private val isInsuranceEnabled: Boolean? = null,

    @Schema(description = "Зарплатный клиент", example = "true")
    private val isSalaryClient: Boolean? = null,

    @Schema(description = "График платежей")
    private val paymentSchedule: List<PaymentScheduleElement>? = null
)