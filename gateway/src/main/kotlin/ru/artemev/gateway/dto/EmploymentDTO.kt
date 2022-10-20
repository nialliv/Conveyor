package ru.artemev.gateway.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.artemev.gateway.model.enums.EmploymentStatus
import ru.artemev.gateway.model.enums.Position
import java.math.BigDecimal

@Schema(description = "Трудоустройство")
data class EmploymentDTO(
    @Schema(description = "Оклад", example = "15000")
    val salary: BigDecimal? = null,

    @Schema(description = "Должность", example = "MID_MANAGER")
    val position: Position? = null,

    @Schema(description = "Общий трудовой стаж", example = "12")
    val workExperienceTotal: Int? = null,

    @Schema(description = "Текущий трудовой стаж", example = "3")
    val workExperienceCurrent: Int? = null,

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    val employmentStatus: EmploymentStatus? = null,

    @Schema(description = "ИНН", example = "123456")
    val employerINN: String? = null
)
