package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Данные для оценки")
public class ScoringDataDTO {
  @NotNull
  @DecimalMin(value = "100000", message = "Сумма не может быть меньше 100 000р")
  @Schema(description = "Сумма кредита", example = "100000")
  private BigDecimal amount;

  @Min(value = 6, message = "Срок займа не может быть меньше 6 месяцев")
  @NotNull
  @Schema(description = "Срок займа", example = "12")
  private Integer term;

  @NotBlank
  @Size(min = 2, max = 100)
  @Schema(description = "Имя", example = "Петя")
  private String firstName;

  @NotBlank
  @Size(min = 2, max = 100)
  @Schema(description = "Фамилия", example = "Петров")
  private String lastName;

  @NotBlank
  @Size(min = 2, max = 100)
  @Schema(description = "Отчество", example = "Петрович")
  private String middleName;

  @NotBlank
  @Schema(description = "Пол", example = "MALE")
  private Gender gender;

  @NotNull
  @Past(message = "Дата может быть только прошлым")
  private LocalDate birthday;

  @NotNull
  @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
  @Schema(description = "Серия паспорта", example = "1234")
  private String passportSeries;

  @NotNull
  @Pattern(regexp = "[\\d]{6}", message = "Не верно введен номер паспорта")
  @Schema(description = "Номер пасорта", example = "567890")
  private String passportNumber;

  @NotBlank
  @Past
  @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
  private LocalDate passportIssueDate;

  @NotBlank
  @Schema(description = "Кем выдан паспорт", example = "Moscow")
  private String passportIssueBranch;

  @NotBlank
  @Schema(description = "Семейное положение", example = "SINGLE")
  private MaritalStatus maritalStatus;

  @NotNull
  @Schema(description = "Количество иждивенцев", example = "1")
  private Integer dependentAmount;

  @NotNull
  @Schema(description = "Трудоустройство")
  private EmploymentDTO employment;

  @NotNull
  @Schema(description = "Учетная запись")
  private String account;

  @NotNull
  @Schema(description = "Страховка", example = "true")
  private Boolean isInsuranceEnabled;

  @NotNull
  @Schema(description = "Зарплатный клиент", example = "true")
  private Boolean isSalaryClient;
}
