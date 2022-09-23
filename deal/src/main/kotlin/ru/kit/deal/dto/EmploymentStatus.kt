package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Рабочий статус")
enum class EmploymentStatus {
    UNEMPLOYED, SELF_EMPLOYED, EMPLOYED, BUSINESS_OWNER
}