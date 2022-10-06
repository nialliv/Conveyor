package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.conveyor.model.enums.EmploymentStatus;
import ru.artemev.conveyor.model.enums.Position;

import java.math.BigDecimal;

@Builder
@Data
@Schema(description = "Трудоустройство")
public class EmploymentDTO {
  @Schema(description = "Оклад", example = "15000")
  private final BigDecimal salary;

  @Schema(description = "Должность", example = "MID_MANAGER")
  private final Position position;

  @Schema(description = "Общий трудовой стаж", example = "12")
  private final Integer workExperienceTotal;

  @Schema(description = "Текущий трудовой стаж", example = "3")
  private final Integer workExperienceCurrent;

  @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
  private EmploymentStatus employmentStatus;

  @Schema(description = "ИНН", example = "123456")
  private String employerINN;
}
