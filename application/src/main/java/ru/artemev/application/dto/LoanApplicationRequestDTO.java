package ru.artemev.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Заявка на кредит")
@JsonDeserialize(builder = LoanApplicationRequestDTO.LoanApplicationRequestDTOBuilder.class)
public class LoanApplicationRequestDTO {
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
  @Size(min = 2, max = 30)
  @Schema(description = "Имя", example = "Петя")
  @JsonProperty("firstName")
  private String firstName;

  @NotNull
  @Size(min = 2, max = 30)
  @Schema(description = "Фамилия", example = "Петров")
  @JsonProperty("lastName")
  private String lastName;

  @NotBlank
  @Size(min = 2, max = 30)
  @Schema(description = "Отчество", example = "Петрович")
  @JsonProperty("middleName")
  private String middleName;

  @Email
  @Schema(description = "Электронная почта", example = "ppetrov@example.com")
  @JsonProperty("email")
  private String email;

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
}
