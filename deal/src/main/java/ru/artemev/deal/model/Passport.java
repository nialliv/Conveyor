package ru.artemev.deal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Schema(description = "Паспорт")
@Builder
public class Passport {

  @NotNull
  @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
  @Schema(description = "Серия паспорта", example = "1234")
  private String series;

  @NotNull
  @Pattern(regexp = "[\\d]{6}", message = "Не верно введена серия паспорта")
  @Schema(description = "Серия паспорта", example = "1234")
  private String number;

  @Past(message = "Дата может быть только прошлым")
  @Schema(description = "Дата выдачи")
  private LocalDate issueDate;

  @Schema(description = "Отделение")
  private String issueBranch;
}
