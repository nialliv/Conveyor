package ru.artemev.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Заявка на кредит")
public class LoanApplicationRequestDTO {
  @NotNull
  @Schema(description = "Сумма кредита", example = "100000")
  private BigDecimal amount;

  @NotNull
  @Schema(description = "Срок займа", example = "12")
  private Integer term;

  @NotBlank
  @Schema(description = "Имя", example = "Петя")
  private String firstName;

  @NotBlank
  @Schema(description = "Фамилия", example = "Петров")
  private String lastName;

  @NotBlank
  @Schema(description = "Отчество", example = "Петрович")
  private String middleName;

  @Schema(description = "Электронная почта", example = "ppetrov@example.com")
  private String email;

  @NotNull
  @Past(message = "Дата может быть только прошлым")
  private LocalDate birthday;

  @NotNull
  @Schema(description = "Серия паспорта", example = "1234")
  private String passportSeries;

  @NotNull
  @Schema(description = "Номер пасорта", example = "567890")
  private String passportNumber;
}
