package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDate

@Data
@Builder
@Schema(description = "График платежей")
class PaymentScheduleElement {
    @Schema(description = "Номер платежа", example = "1")
    private val number: Int? = null

    @Schema(description = "Дата платежа", example = "2000-01-01")
    private val date: LocalDate? = null

    @Schema(description = "Всего к оплате ", example = "1000")
    private val totalPayment: BigDecimal? = null

    @Schema(description = "Выплата процентов ", example = "1000")
    private val interestPayment: BigDecimal? = null

    @Schema(description = "Выплата долга ", example = "1000")
    private val debtPayment: BigDecimal? = null

    @Schema(description = "Оставшийся долг ", example = "1000")
    private val remainingDebt: BigDecimal? = null
}