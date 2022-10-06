package ru.artemev.application.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Рабочий статус")
@AllArgsConstructor
public enum EmploymentStatus {
  UNEMPLOYED("Безработный"),
  SELF_EMPLOYED("Частный предприниматель"),
  EMPLOYED("Рабочий"),
  BUSINESS_OWNER("Владелец бизнеса");

  private final String description;
}
