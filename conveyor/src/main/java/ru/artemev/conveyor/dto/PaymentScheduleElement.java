package ru.artemev.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "График платежей")
public class PaymentScheduleElement {
  @Schema(description = "Номер платежа", example = "1")
  private Integer number;

  @Schema(description = "Дата платежа", example = "2000-01-01")
  private LocalDate date;

  @Schema(description = "Всего к оплате ", example = "1000")
  private BigDecimal totalPayment;

  @Schema(description = "Выплата процентов ", example = "1000")
  private BigDecimal interestPayment;

  @Schema(description = "Выплата долга ", example = "1000")
  private BigDecimal debtPayment;

  @Schema(description = "Оставшийся долг ", example = "1000")
  private BigDecimal remainingDebt;
}
