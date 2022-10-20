package ru.artemev.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.conveyor.model.enums.Gender;
import ru.artemev.conveyor.model.enums.MaritalStatus;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Данные для оценки")
@JsonDeserialize(builder = ScoringDataDTO.ScoringDataDTOBuilder.class)
public class ScoringDataDTO {
  @NotNull
  @DecimalMin(value = "10000", message = "Сумма не может быть меньше 10 000р")
  @Schema(description = "Сумма кредита", example = "10000")
  @JsonProperty("amount")
  private BigDecimal amount;

  @Min(value = 6, message = "Срок займа не может быть меньше 6 месяцев")
  @NotNull
  @Schema(description = "Срок займа", example = "12")
  @JsonProperty("term")
  private Integer term;

  @NotNull
  @Size(min = 2, max = 100)
  @Schema(description = "Имя", example = "Петя")
  @JsonProperty("firstName")
  private String firstName;

  @NotNull
  @Size(min = 2, max = 100)
  @Schema(description = "Фамилия", example = "Петров")
  @JsonProperty("lastName")
  private String lastName;

  @NotNull
  @Size(min = 2, max = 100)
  @Schema(description = "Отчество", example = "Петрович")
  @JsonProperty("middleName")
  private String middleName;

  @NotNull
  @Schema(description = "Пол", example = "MALE")
  @JsonProperty("gender")
  private Gender gender;

  @NotNull
  @Past(message = "Дата может быть только прошлым")
  @JsonProperty("birthday")
  private LocalDate birthday;

  @NotNull
  @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
  @Schema(description = "Серия паспорта", example = "1234")
  @JsonProperty("passportSeries")
  private String passportSeries;

  @NotNull
  @Pattern(regexp = "[\\d]{6}", message = "Не верно введен номер паспорта")
  @Schema(description = "Номер пасорта", example = "567890")
  @JsonProperty("passportNumber")
  private String passportNumber;

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
  @Schema(description = "Семейное положение", example = "SINGLE")
  @JsonProperty("maritalStatus")
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев", example = "1")
  @JsonProperty("dependentAmount")
  private Integer dependentAmount;

  @NotNull
  @Schema(description = "Трудоустройство")
  @JsonProperty("employment")
  private EmploymentDTO employment;

  @NotNull
  @Schema(description = "Учетная запись")
  @JsonProperty("account")
  private String account;

  @NotNull
  @Schema(description = "Страховка", example = "true")
  @JsonProperty("isInsuranceEnabled")
  private Boolean isInsuranceEnabled;

  @NotNull
  @Schema(description = "Зарплатный клиент", example = "true")
  @JsonProperty("isSalaryClient")
  private Boolean isSalaryClient;
}
