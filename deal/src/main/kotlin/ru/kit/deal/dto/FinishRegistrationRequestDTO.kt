package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@Schema(description = "Запрос на завершение регистрации")
data class FinishRegistrationRequestDTO(
    @NotBlank
    @Schema(description = "Пол", example = "MALE")
    val gender: Gender? = null,

    @NotBlank
    @Schema(description = "Семейное положение", example = "SINGLE")
    val maritalStatus: MaritalStatus? = null,

    @NotNull
    @Schema(description = "Количество иждивенцев", example = "1")
    val dependentAmount: Int? = null,

    @NotBlank
    @Past
    @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
    val passportIssueDate: LocalDate? = null,

    @NotBlank
    @Schema(description = "Кем выдан паспорт", example = "Moscow")
    val passportIssueBranch: String? = null,

    @NotNull
    @Schema(description = "Трудоустройство")
    val employment: EmploymentDTO? = null,

    @NotNull
    @Schema(description = "Учетная запись")
    val account: String? = null,
)
