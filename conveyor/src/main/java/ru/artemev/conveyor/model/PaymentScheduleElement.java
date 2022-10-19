package ru.artemev.conveyor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "График платежей")
@JsonDeserialize(builder = PaymentScheduleElement.PaymentScheduleElementBuilder.class)
public class PaymentScheduleElement {
  @Schema(description = "Номер платежа", example = "1")
  @JsonProperty("number")
  private Integer number;

  @Schema(description = "Дата платежа", example = "2000-01-01")
  @JsonProperty("date")
  private LocalDate date;

  @Schema(description = "Всего к оплате ", example = "1000")
  @JsonProperty("totalPayment")
  private BigDecimal totalPayment;

  @Schema(description = "Выплата процентов ", example = "1000")
  @JsonProperty("interestPayment")
  private BigDecimal interestPayment;

  @Schema(description = "Выплата долга ", example = "1000")
  @JsonProperty("debtPayment")
  private BigDecimal debtPayment;

  @Schema(description = "Оставшийся долг ", example = "1000")
  @JsonProperty("remainingDebt")
  private BigDecimal remainingDebt;
}
