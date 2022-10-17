package ru.artemev.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.application.model.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
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
  private final Boolean insuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  private final Boolean salaryClient;

  @Schema(description = "График платежей")
  private final List<PaymentScheduleElement> paymentSchedule;
}
