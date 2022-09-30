package ru.artemev.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рабочий статус")
public enum EmploymentStatus {
  @Schema(description = "Безработный")
  UNEMPLOYED,
  @Schema(description = "Частный предприниматель")
  SELF_EMPLOYED,
  @Schema(description = "Рабочий")
  EMPLOYED,
  @Schema(description = "Владелец бизнеса")
  BUSINESS_OWNER
}
