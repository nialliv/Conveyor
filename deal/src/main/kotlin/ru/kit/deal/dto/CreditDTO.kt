package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal


@Schema(description = "Кредит")
data class CreditDTO(
    @Schema(description = "Сумма", example = "10000")
    val amount: BigDecimal? = null,

    @Schema(description = "Срок займа", example = "6")
    val term: Int? = null,

    @Schema(description = "Ежемесячный платеж", example = "1500")
    val monthlyPayment: BigDecimal? = null,

    @Schema(description = "Процент кредита", example = "15")
    val rate: BigDecimal? = null,

    @Schema(description = "Полная стоимость кредита", example = "15")
    val psk: BigDecimal? = null,

    @Schema(description = "Страховка", example = "true")
    val isInsuranceEnabled: Boolean? = null,

    @Schema(description = "Зарплатный клиент", example = "true")
    val isSalaryClient: Boolean? = null,

    @Schema(description = "График платежей")
    val paymentSchedule: List<PaymentScheduleElement>? = null
)