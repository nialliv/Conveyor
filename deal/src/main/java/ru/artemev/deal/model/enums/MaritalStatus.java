package ru.artemev.deal.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Семейное положение")
@AllArgsConstructor
public enum MaritalStatus {
  MARRIED("Замужем/женат"),
  DIVORCED("В разводе"),
  SINGLE("Холост"),
  WIDOW_WIDOWER("Вдова");

  private final String description;
}
