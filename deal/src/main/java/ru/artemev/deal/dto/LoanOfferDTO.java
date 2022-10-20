package ru.artemev.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Предложение по кредиту")
@JsonDeserialize(builder = LoanOfferDTO.LoanOfferDTOBuilder.class)
public class LoanOfferDTO {
  @Schema(description = "Итоговая сумма", example = "15000")
  @JsonProperty("totalAmount")
  private BigDecimal totalAmount;

  @Schema(description = "Срок займа", example = "10")
  @JsonProperty("term")
  private Integer term;

  @Schema(description = "Ежемесячный платеж", example = "1500")
  @JsonProperty("monthlyPayment")
  private BigDecimal monthlyPayment;

  @Schema(description = "Процентная ставка", example = "10")
  @JsonProperty("rate")
  private BigDecimal rate;

  @Schema(description = "Страховка", example = "true")
  @JsonProperty("isInsuranceEnabled")
  private Boolean isInsuranceEnabled;

  @Schema(description = "Зарплатный клиент", example = "true")
  @JsonProperty("isSalaryClient")
  private Boolean isSalaryClient;

  @Schema(description = "Номер предложения", example = "4")
  @JsonProperty("applicationId")
  private Long applicationId;

  @Schema(description = "Запрашиваемая сумма", example = "100000")
  @JsonProperty("requestedAmount")
  private BigDecimal requestedAmount;
}
