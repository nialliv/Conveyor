package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Статус кредита")
enum class CreditStatus {
    CALCULATED, ISSUED
}