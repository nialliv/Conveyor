package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "График платежей")
data class PaymentScheduleElement(
    @Schema(description = "Номер платежа", example = "1")
    val number: Int? = null,

    @Schema(description = "Дата платежа", example = "2000-01-01")
    val date: LocalDate? = null,

    @Schema(description = "Всего к оплате ", example = "1000")
    val totalPayment: BigDecimal? = null,

    @Schema(description = "Выплата процентов ", example = "1000")
    val interestPayment: BigDecimal? = null,

    @Schema(description = "Выплата долга ", example = "1000")
    val debtPayment: BigDecimal? = null,

    @Schema(description = "Оставшийся долг ", example = "1000")
    val remainingDebt: BigDecimal? = null,
)