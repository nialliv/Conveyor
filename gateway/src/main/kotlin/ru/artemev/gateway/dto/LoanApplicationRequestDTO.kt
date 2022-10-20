package ru.artemev.gateway.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@Schema(description = "Заявка на кредит")
data class LoanApplicationRequestDTO(
    @Schema(description = "Сумма кредита", example = "10000")
    val amount: @NotNull BigDecimal? = null,

    @Schema(description = "Срок займа", example = "12")
    val term: @NotNull Int? = null,

    @Schema(description = "Имя", example = "Петя")
    val firstName: @NotNull String? = null,

    @Schema(description = "Фамилия", example = "Петров")
    val lastName: @NotNull String? = null,

    @Schema(description = "Отчество", example = "Петрович")
    val middleName: @NotNull String? = null,

    @Schema(description = "Электронная почта", example = "ppetrov@example.com")
    val email: String? = null,
    val birthday: @NotNull @Past(message = "Дата может быть только прошлым") LocalDate? = null,

    @Schema(description = "Серия паспорта", example = "1234")
    val passportSeries: @NotNull String? = null,

    @Schema(description = "Номер пасорта", example = "567890")
    val passportNumber: @NotNull String? = null,
)