package ru.artemev.application.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Рабочий статус")
@AllArgsConstructor
@Getter
public enum EmploymentStatus {
  UNEMPLOYED("Безработный"),
  SELF_EMPLOYED("Частный предприниматель"),
  EMPLOYED("Рабочий"),
  BUSINESS_OWNER("Владелец бизнеса");

  private final String description;
}
