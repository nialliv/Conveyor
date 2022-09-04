package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Кредит")
public class CreditDTO {

  @Schema(description = "Сумма", example = "10000")
  private final BigDecimal amount;

  @Schema(description = "Срок займа", example = "6")
  private final Integer term;

  @Schema(description = "Ежемесячный платеж", example = "1500")
  private final BigDecimal monthlyPayment;

  @Schema(description = "Процент кредита", example = "15")
  private final BigDecimal rate;

  @Schema(description = "Полная стоимость кредита", example = "15")
  private final BigDecimal psk;

  @Schema(description = "Страховка", example = "true")
  private final Boolean isInsuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  private final Boolean isSalaryClient;

  @Schema(description = "График платежей")
  private final List<PaymentScheduleElement> paymentSchedule;
}
