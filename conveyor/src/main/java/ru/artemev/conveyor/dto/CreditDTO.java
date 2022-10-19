package ru.artemev.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.artemev.conveyor.model.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Кредит")
@JsonDeserialize(builder = CreditDTO.CreditDTOBuilder.class)
public class CreditDTO {

  @Schema(description = "Сумма", example = "10000")
  @JsonProperty("amount")
  private BigDecimal amount;

  @Schema(description = "Срок займа", example = "6")
  @JsonProperty("term")
  private Integer term;

  @Schema(description = "Ежемесячный платеж", example = "1500")
  @JsonProperty("monthlyPayment")
  private BigDecimal monthlyPayment;

  @Schema(description = "Процент кредита", example = "15")
  @JsonProperty("rate")
  private BigDecimal rate;

  @Schema(description = "Полная стоимость кредита", example = "15")
  @JsonProperty("psk")
  private BigDecimal psk;

  @Schema(description = "Страховка", example = "true")
  @JsonProperty("isInsuranceEnabled")
  private Boolean isInsuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  @JsonProperty("isSalaryClient")
  private Boolean isSalaryClient;

  @Schema(description = "График платежей")
  @JsonProperty("paymentSchedule")
  private List<PaymentScheduleElement> paymentSchedule;
}
