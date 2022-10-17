package ru.artemev.gateway.model.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Должность")
enum class Position(val description: String) {
    WORKER("Рабочий"),
    MID_MANAGER("Менеджер среднего звена"),
    TOP_MANAGER("Топ-менеджер"),
    OWNER("Директор")
}