package ru.artemev.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.deal.model.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Кредит")
@JsonDeserialize(builder = CreditDTO.CreditDTOBuilder.class)
public class CreditDTO {

  @Schema(description = "Сумма", example = "10000")
  @JsonProperty("amount")
  private final BigDecimal amount;

  @Schema(description = "Срок займа", example = "6")
  @JsonProperty("term")
  private final Integer term;

  @Schema(description = "Ежемесячный платеж", example = "1500")
  @JsonProperty("monthlyPayment")
  private final BigDecimal monthlyPayment;

  @Schema(description = "Процент кредита", example = "15")
  @JsonProperty("rate")
  private final BigDecimal rate;

  @Schema(description = "Полная стоимость кредита", example = "15")
  @JsonProperty("psk")
  private final BigDecimal psk;

  @Schema(description = "Страховка", example = "true")
  @JsonProperty("isInsuranceEnabled")
  private final Boolean isInsuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  @JsonProperty("isSalaryClient")
  private final Boolean isSalaryClient;

  @Schema(description = "График платежей")
  @JsonProperty("paymentSchedule")
  private final List<PaymentScheduleElement> paymentSchedule;
}
