package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Семейное положение")
enum class MaritalStatus {
    MARRIED, DIVORCED, SINGLE, WIDOW_WIDOWER
}