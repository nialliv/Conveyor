package ru.artemev.deal.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(description = "Статус кредита")
public enum CreditStatus {
  CALCULATED("Подсчитывается"),
  ISSUED("Выдан");

  private final String description;
}
