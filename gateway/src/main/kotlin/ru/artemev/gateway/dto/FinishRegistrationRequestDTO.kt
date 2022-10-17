package ru.artemev.gateway.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.artemev.gateway.model.enums.Gender
import ru.artemev.gateway.model.enums.MaritalStatus
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@Schema(description = "Запрос на завершение регистрации")
data class FinishRegistrationRequestDTO(
    @Schema(description = "Пол", example = "MALE")
    val gender: @NotNull Gender,

    @Schema(description = "Семейное положение", example = "SINGLE")
    val maritalStatus: @NotNull MaritalStatus,

    @Schema(description = "Количество иждивенцев", example = "1")
    val dependentAmount: @NotNull Int,

    @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
    val passportIssueDate: @NotNull @Past LocalDate,

    @Schema(description = "Кем выдан паспорт", example = "Moscow")
    val passportIssueBranch: @NotNull String,

    @Schema(description = "Трудоустройство")
    val employment: @NotNull EmploymentDTO,

    @Schema(description = "Учетная запись")
    val account: @NotNull String
)