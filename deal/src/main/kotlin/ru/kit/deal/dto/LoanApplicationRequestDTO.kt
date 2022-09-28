package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

@Schema(description = "Заявка на кредит")
data class LoanApplicationRequestDTO(
    @NotNull
    @DecimalMin(value = "100000", message = "Сумма не может быть меньше 100 000р")
    @Schema(description = "Сумма кредита", example = "100000")
    val amount: BigDecimal? = null,

    @Min(value = 6, message = "Срок займа не может быть меньше 6 месяцев")
    @NotNull
    @Schema(description = "Срок займа", example = "12")
    val term: Int? = null,

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Имя", example = "Петя")
    val firstName: String? = null,

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Фамилия", example = "Петров")
    val lastName: String? = null,

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Отчество", example = "Петрович")
    val middleName: String? = null,

    @Email
    @Schema(description = "Электронная почта", example = "ppetrov@example.com")
    val email: String? = null,

    @NotNull
    @Past(message = "Дата может быть только прошлым")
    val birthday: LocalDate? = null,

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
    @Schema(description = "Серия паспорта", example = "1234")
    val passportSeries: String? = null,

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Не верно введен номер паспорта")
    @Schema(description = "Номер пасорта", example = "567890")
    val passportNumber: String? = null,
)