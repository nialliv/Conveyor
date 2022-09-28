package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Рабочий статус")
enum class EmploymentStatus {
    UNEMPLOYED, SELF_EMPLOYED, EMPLOYED, BUSINESS_OWNER
}