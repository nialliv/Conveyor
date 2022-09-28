package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Pattern

@Schema(description = "Паспорт")
data class Passport(
    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
    @Schema(description = "Серия паспорта", example = "1234")
    val series: String? = null,

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Не верно введена серия паспорта")
    @Schema(description = "Серия паспорта", example = "1234")
    val number: String? = null,


    @Past(message = "Дата может быть только прошлым")
    @Schema(description = "Дата выдачи")
    val issue_date: LocalDate? = null,


    @Schema(description = "Отделение")
    val issue_branch: String? = null,
)