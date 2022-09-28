package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Пол")
enum class Gender {
    MALE, FEMALE, NOT_BINARY
}