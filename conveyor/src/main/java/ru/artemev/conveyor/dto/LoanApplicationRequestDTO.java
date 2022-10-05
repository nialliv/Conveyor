package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
<<<<<<< HEAD
import jakarta.validation.constraints.*;
import lombok.Data;

=======
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
>>>>>>> feature/MS-conveyor/mvp1
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
<<<<<<< HEAD
=======
@Builder
>>>>>>> feature/MS-conveyor/mvp1
@Schema(description = "Заявка на кредит")
public class LoanApplicationRequestDTO {
  @NotNull
  @DecimalMin(value = "100000", message = "Сумма не может быть меньше 100 000р")
  @Schema(description = "Сумма кредита", example = "100000")
  private BigDecimal amount;

  @Min(value = 6, message = "Срок займа не может быть меньше 6 месяцев")
  @NotNull
  @Schema(description = "Срок займа", example = "12")
  private Integer term;

<<<<<<< HEAD
  @NotBlank
=======
  @NotNull
>>>>>>> feature/MS-conveyor/mvp1
  @Size(min = 2, max = 100)
  @Schema(description = "Имя", example = "Петя")
  private String firstName;

<<<<<<< HEAD
  @NotBlank
=======
  @NotNull
>>>>>>> feature/MS-conveyor/mvp1
  @Size(min = 2, max = 100)
  @Schema(description = "Фамилия", example = "Петров")
  private String lastName;

<<<<<<< HEAD
  @NotBlank
=======
  @NotNull
>>>>>>> feature/MS-conveyor/mvp1
  @Size(min = 2, max = 100)
  @Schema(description = "Отчество", example = "Петрович")
  private String middleName;

  @Email
  @Schema(description = "Электронная почта", example = "ppetrov@example.com")
  private String email;

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
}
