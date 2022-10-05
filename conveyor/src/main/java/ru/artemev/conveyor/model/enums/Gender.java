package ru.artemev.conveyor.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Пол")
@AllArgsConstructor
public enum Gender {
  MALE("Мужчина"),
  FEMALE("Женщина"),
  NOT_BINARY("Другое");
  private final String description;
}
