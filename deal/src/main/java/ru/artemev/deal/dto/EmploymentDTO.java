package ru.artemev.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.deal.model.enums.EmploymentStatus;
import ru.artemev.deal.model.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Трудоустройство")
@JsonDeserialize(builder = EmploymentDTO.EmploymentDTOBuilder.class)
public class EmploymentDTO {
  @Schema(description = "Оклад", example = "15000")
  @JsonProperty("salary")
  private final BigDecimal salary;

  @Schema(description = "Должность", example = "MID_MANAGER")
  @JsonProperty("position")
  private final Position position;

  @Schema(description = "Общий трудовой стаж", example = "12")
  @JsonProperty("workExperienceTotal")
  private final Integer workExperienceTotal;

  @Schema(description = "Текущий трудовой стаж", example = "3")
  @JsonProperty("workExperienceCurrent")
  private final Integer workExperienceCurrent;

  @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
  @JsonProperty("employmentStatus")
  private EmploymentStatus employmentStatus;

  @Schema(description = "ИНН", example = "123456")
  @JsonProperty("employerINN")
  private String employerINN;
}
