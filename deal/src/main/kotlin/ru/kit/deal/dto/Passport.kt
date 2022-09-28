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
    private val series: String? = null,

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Не верно введена серия паспорта")
    @Schema(description = "Серия паспорта", example = "1234")
    private val number: String? = null,

    @NotNull
    @Past(message = "Дата может быть только прошлым")
    @Schema(description = "Дата выдачи")
    private val issue_date: LocalDate? = null,

    @NotNull
    @Schema(description = "Отделение")
    private val issue_branch: String? = null,
)