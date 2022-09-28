package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Трудоустройство")
data class EmploymentDTO(
    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private val employmentStatus: EmploymentStatus? = null,

    @Schema(description = "ИНН", example = "123456")
    private val employerINN: String? = null,

    @Schema(description = "Оклад", example = "15000")
    private val salary: BigDecimal? = null,

    @Schema(description = "Должность", example = "MANAGER")
    private val position: Position? = null,

    @Schema(description = "Общий трудовой стаж", example = "12")
    private val workExperienceTotal: Int? = null,

    @Schema(description = "Текущий трудовой стаж", example = "3")
    private val workExperienceCurrent: Int? = null,
)