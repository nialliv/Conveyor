package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Семейное положение")
enum class MaritalStatus {
    MARRIED, DIVORCED, SINGLE, WIDOW_WIDOWER
}