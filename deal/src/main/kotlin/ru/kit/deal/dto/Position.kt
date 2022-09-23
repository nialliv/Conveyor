package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Должность")
enum class Position {
    WORKER, MID_MANAGER, TOP_MANAGER, OWNER
}