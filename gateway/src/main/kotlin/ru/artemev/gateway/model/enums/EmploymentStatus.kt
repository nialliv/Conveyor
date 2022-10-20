package ru.artemev.gateway.model.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Рабочий статус")
enum class EmploymentStatus(val description: String) {
    UNEMPLOYED("Безработный"),
    SELF_EMPLOYED("Частный предприниматель"),
    EMPLOYED("Рабочий"),
    BUSINESS_OWNER("Владелец бизнеса")
}
