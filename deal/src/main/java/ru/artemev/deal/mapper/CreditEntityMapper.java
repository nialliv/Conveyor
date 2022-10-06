package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.entity.CreditEntity;
import ru.artemev.deal.model.enums.CreditStatus;

@Mapper
public abstract class CreditEntityMapper {
  public static CreditEntity toClientEntity(CreditDTO creditDTO) {
    return CreditEntity.builder()
        .amount(creditDTO.getAmount())
            .term(creditDTO.getTerm())
        .monthlyPayment(creditDTO.getMonthlyPayment())
        .rate(creditDTO.getRate())
        .psk(creditDTO.getPsk())
        .paymentSchedule(creditDTO.getPaymentSchedule())
        .insuranceEnabled(creditDTO.getInsuranceEnabled())
        .salaryClient(creditDTO.getSalaryClient())
        .creditStatus(CreditStatus.CALCULATED)
        .build();
  }
}
