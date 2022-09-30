package ru.artemev.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Предложение по кредиту")
public class LoanOfferDTO {
  @Schema(description = "Номер предложения", example = "4")
  private Long applicationId;

  @Schema(description = "Запрашиваемая сумма", example = "100000")
  private BigDecimal requestedAmount;

  @Schema(description = "Итоговая сумма", example = "15000")
  private final BigDecimal totalAmount;

  @Schema(description = "Срок займа", example = "10")
  private final Integer term;

  @Schema(description = "Ежемесячный платеж", example = "1500")
  private final BigDecimal monthlyPayment;

  @Schema(description = "Процентная ставка", example = "10")
  private final BigDecimal rate;

  @Schema(description = "Страховка", example = "true")
  private final boolean insuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  private final boolean salaryClient;
}
