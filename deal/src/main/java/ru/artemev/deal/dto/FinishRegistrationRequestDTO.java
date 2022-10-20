package ru.artemev.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.deal.model.enums.Gender;
import ru.artemev.deal.model.enums.MaritalStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Запрос на завершение регистрации")
@JsonDeserialize(builder = FinishRegistrationRequestDTO.FinishRegistrationRequestDTOBuilder.class)
public class FinishRegistrationRequestDTO {
  @NotNull
  @Schema(description = "Пол", example = "MALE")
  @JsonProperty("gender")
  private Gender gender;

  @NotNull
  @Schema(description = "Семейное положение", example = "SINGLE")
  @JsonProperty("maritalStatus")
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев", example = "1")
  @JsonProperty("dependentAmount")
  private Integer dependentAmount;

  @NotNull
  @Past
  @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
  @JsonProperty("passportIssueDate")
  private LocalDate passportIssueDate;

  @NotNull
  @Schema(description = "Кем выдан паспорт", example = "Moscow")
  @JsonProperty("passportIssueBranch")
  private String passportIssueBranch;

  @NotNull
  @Schema(description = "Трудоустройство")
  @JsonProperty("employment")
  private EmploymentDTO employment;

  @NotNull
  @Schema(description = "Учетная запись")
  @JsonProperty("account")
  private String account;
}
