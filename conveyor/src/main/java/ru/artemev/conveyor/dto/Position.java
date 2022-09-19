package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Должность")
public enum Position {
    @Schema(description = "Рабочий")
    WORKER,
    @Schema(description = "Менеджер среднего звена")
    MID_MANAGER,
    @Schema(description = "Топ-менеджер")
    TOP_MANAGER,
    @Schema(description = "Директор")
    OWNER
}
