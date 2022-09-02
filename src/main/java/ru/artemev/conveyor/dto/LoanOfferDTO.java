package ru.artemev.conveyor.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanOfferDTO {
    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnable;
    private Boolean isSalaryClient;

}