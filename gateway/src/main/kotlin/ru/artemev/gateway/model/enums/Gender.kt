package ru.artemev.gateway.model.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Пол")
enum class Gender(val description: String) {
    MALE("Мужчина"),
    FEMALE("Женщина"),
    NOT_BINARY("Другое")
}