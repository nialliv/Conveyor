package ru.artemev.gateway.model.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Семейное положение")
enum class MaritalStatus(val description: String) {
    MARRIED("Замужем/женат"),
    DIVORCED("В разводе"),
    SINGLE("Холост"),
    WIDOW_WIDOWER("Вдова")
}