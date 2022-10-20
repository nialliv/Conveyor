package ru.artemev.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import ru.artemev.gateway.model.enums.Gender
import ru.artemev.gateway.model.enums.MaritalStatus
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@Schema(description = "Запрос на завершение регистрации")
data class FinishRegistrationRequestDTO(
    @Schema(description = "Пол", example = "MALE")
    @JsonProperty("gender")
    val gender: @NotNull Gender,

    @Schema(description = "Семейное положение", example = "SINGLE")
    @JsonProperty("maritalStatus")
    val maritalStatus: @NotNull MaritalStatus,

    @Schema(description = "Количество иждивенцев", example = "1")
    @JsonProperty("dependentAmount")
    val dependentAmount: @NotNull Int,

    @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
    @JsonProperty("passportIssueDate")
    val passportIssueDate: @NotNull @Past LocalDate,

    @Schema(description = "Кем выдан паспорт", example = "Moscow")
    @JsonProperty("passportIssueBranch")
    val passportIssueBranch: @NotNull String,

    @Schema(description = "Трудоустройство")
    @JsonProperty("employment")
    val employment: @NotNull EmploymentDTO,

    @Schema(description = "Учетная запись")
    @JsonProperty("account")
    val account: @NotNull String
)
