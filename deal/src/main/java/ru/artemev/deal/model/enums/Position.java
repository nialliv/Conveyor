package ru.artemev.deal.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Должность")
@AllArgsConstructor
public enum Position {
  WORKER("Рабочий"),
  MID_MANAGER("Менеджер среднего звена"),
  TOP_MANAGER("Топ-менеджер"),
  OWNER("Директор");

  private final String description;
}
