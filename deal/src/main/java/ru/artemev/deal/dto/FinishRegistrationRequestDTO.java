package ru.artemev.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.artemev.deal.model.enums.Gender;
import ru.artemev.deal.model.enums.MaritalStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Schema(description = "Запрос на завершение регистрации")
public class FinishRegistrationRequestDTO {
  @NotBlank
  @Schema(description = "Пол", example = "MALE")
  private Gender gender;

  @NotBlank
  @Schema(description = "Семейное положение", example = "SINGLE")
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев", example = "1")
  private Integer dependentAmount;

  @NotBlank
  @Past
  @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
  private LocalDate passportIssueDate;

  @NotBlank
  @Schema(description = "Кем выдан паспорт", example = "Moscow")
  private String passportIssueBranch;

  @NotNull
  @Schema(description = "Трудоустройство")
  private EmploymentDTO employment;

  @NotNull
  @Schema(description = "Учетная запись")
  private String account;
}
