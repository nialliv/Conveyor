package ru.artemev.conveyor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.EmploymentDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.exception.ValidationException;
import ru.artemev.conveyor.model.PaymentScheduleElement;
import ru.artemev.conveyor.model.enums.EmploymentStatus;
import ru.artemev.conveyor.model.enums.Gender;
import ru.artemev.conveyor.model.enums.MaritalStatus;
import ru.artemev.conveyor.model.enums.Position;
import ru.artemev.conveyor.service.ConveyorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ConveyorServiceImp implements ConveyorService {

  @Value("${rate.base}")
  private int baseRate;

  @Override
  public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
    log.info("======Started getOffers=======");
    log.info("Received -> " + loanApplicationRequestDTO);
    List<LoanOfferDTO> loanOfferDTOList =
        Stream.of(
                getCompletedOffers(1L, false, false, loanApplicationRequestDTO),
                getCompletedOffers(2L, false, true, loanApplicationRequestDTO),
                getCompletedOffers(3L, true, false, loanApplicationRequestDTO),
                getCompletedOffers(4L, true, true, loanApplicationRequestDTO))
            .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
            .collect(Collectors.toList());
    log.info("Return -> " + loanOfferDTOList);
    log.info("======Finished getOffers=======");
    return loanOfferDTOList;
  }

  @Override
  public CreditDTO getCreditDto(ScoringDataDTO scoringDataDTO) {
    log.info("======Started getCreditDto=======");
    log.info("Received -> " + scoringDataDTO);
    if (!validateData(scoringDataDTO)) throw new ValidationException("Ошибка валидации");
    BigDecimal rate = BigDecimal.valueOf(baseRate);
    rate = getRateByInsurance(scoringDataDTO.getIsInsuranceEnabled(), rate);
    rate = getRateBySalary(scoringDataDTO.getIsSalaryClient(), rate);
    rate = getRateByEmployment(scoringDataDTO.getEmployment(), rate);
    rate = getRateByMaritalStatus(scoringDataDTO.getMaritalStatus(), rate);
    rate = getRateByDependentAmount(scoringDataDTO.getDependentAmount(), rate);
    rate = getRateByGender(scoringDataDTO, rate);
    Integer term = scoringDataDTO.getTerm();
    BigDecimal psk = getPsk(scoringDataDTO, rate);
    BigDecimal monthlyPayment = psk.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
    List<PaymentScheduleElement> paymentScheduleElements =
        getPaymentSchedule(scoringDataDTO.getAmount(), term, psk, monthlyPayment, rate);
    CreditDTO creditDTO =
        CreditDTO.builder()
            .amount(scoringDataDTO.getAmount())
            .term(term)
            .rate(rate)
            .psk(psk)
            .monthlyPayment(monthlyPayment)
            .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
            .isSalaryClient(scoringDataDTO.getIsSalaryClient())
            .paymentSchedule(paymentScheduleElements)
            .build();
    log.info("Return -> " + creditDTO);
    log.info("======Finished getCreditDto=======");
    return creditDTO;
  }

  private List<PaymentScheduleElement> getPaymentSchedule(
      BigDecimal amount, Integer term, BigDecimal psk, BigDecimal monthlyPayment, BigDecimal rate) {
    List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();
    for (int i = 1; i <= term; i++) {
      LocalDate date = LocalDate.now().plusMonths(i);
      BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(i));
      BigDecimal interestPayment =
          (amount.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN)))
              .divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
      BigDecimal debtPayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
      BigDecimal remainingDebt = psk.subtract(totalPayment).setScale(2, RoundingMode.HALF_EVEN);
      paymentScheduleElements.add(
          PaymentScheduleElement.builder()
              .number(i)
              .date(date)
              .totalPayment(totalPayment)
              .interestPayment(interestPayment)
              .debtPayment(debtPayment)
              .remainingDebt(remainingDebt)
              .build());
    }
    return paymentScheduleElements;
  }

  private LoanOfferDTO getCompletedOffers(
      Long applicationId,
      boolean isInsuranceEnabled,
      boolean isSalaryClient,
      LoanApplicationRequestDTO loanApplicationRequestDTO) {
    log.info("Complete offer calculation started");
    if (Period.between(loanApplicationRequestDTO.getBirthday(), LocalDate.now()).getYears() <= 18)
      throw new ValidationException("Клиенту нет 18 лет");
    BigDecimal rate = BigDecimal.valueOf(baseRate);
    rate = getRateByInsurance(isInsuranceEnabled, rate);
    rate = getRateBySalary(isSalaryClient, rate);
    BigDecimal requestedAmount = loanApplicationRequestDTO.getAmount();
    Integer term = loanApplicationRequestDTO.getTerm();

    BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
    BigDecimal creditCoast =
        (requestedAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))
            .multiply(monthlyPercent)
            .multiply(BigDecimal.valueOf(term))
            .setScale(2, RoundingMode.HALF_EVEN);
    BigDecimal totalAmount = requestedAmount.add(creditCoast);
    BigDecimal monthlyPayment =
        totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);

    LoanOfferDTO loanOfferDTO =
        LoanOfferDTO.builder()
            .applicationId(applicationId)
            .requestedAmount(requestedAmount)
            .totalAmount(totalAmount)
            .term(term)
            .monthlyPayment(monthlyPayment)
            .rate(rate)
            .isInsuranceEnabled(isInsuranceEnabled)
            .isSalaryClient(isSalaryClient)
            .build();
    log.info("Offer calculation completed");
    return loanOfferDTO;
  }

  /*
  Безработный → отказ;
  Сумма займа больше, чем 20 зарплат → отказ
  Возраст менее 20 или более 60 лет → отказ
  Общий стаж менее 12 месяцев → отказ; Текущий стаж менее 3 месяцев → отказ
  */
  private boolean validateData(ScoringDataDTO scoringDataDTO) {
    log.info("Start validate");
    int age = Period.between(scoringDataDTO.getBirthday(), LocalDate.now()).getYears();
    if (20 > age || age > 60) throw new ValidationException("Возраст менее 20 или более 60 лет");
    if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED)
      throw new ValidationException("Клиент безработный");
    if (scoringDataDTO.getEmployment().getSalary().multiply(BigDecimal.valueOf(20)).intValue()
        < scoringDataDTO.getAmount().intValue())
      throw new ValidationException("Сумма займа больше, чем 20 зарплат");
    if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < 12)
      throw new ValidationException("Общий стаж менее 12 месяцев");
    if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3)
      throw new ValidationException("Текущий стаж менее 3 месяцев");
    log.info("Finish validate");
    return true;
  }

  private BigDecimal getRateByGender(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
    int age = Period.between(scoringDataDTO.getBirthday(), LocalDate.now()).getYears();
    if (scoringDataDTO.getGender().equals(Gender.FEMALE) && (35 >= age || age >= 60)) {
      rate = rate.subtract(BigDecimal.valueOf(3));
    } else if (scoringDataDTO.getGender().equals(Gender.MALE) && (30 >= age || age >= 55)) {
      rate = rate.subtract(BigDecimal.valueOf(3));
    } else if (scoringDataDTO.getGender().equals(Gender.NOT_BINARY))
      rate = rate.add(BigDecimal.valueOf(3));
    return rate;
  }

  private BigDecimal getRateByDependentAmount(Integer dependentAmount, BigDecimal rate) {
    if (dependentAmount > 1) rate = rate.add(BigDecimal.valueOf(1));
    return rate;
  }

  private BigDecimal getRateByMaritalStatus(MaritalStatus maritalStatus, BigDecimal rate) {
    if (maritalStatus.equals(MaritalStatus.MARRIED)) rate = rate.subtract(BigDecimal.valueOf(3));
    else if (maritalStatus.equals(MaritalStatus.DIVORCED)) rate = rate.add(BigDecimal.valueOf(1));
    return rate;
  }

  private BigDecimal getRateByEmployment(EmploymentDTO employment, BigDecimal rate) {
    if (employment.getEmploymentStatus().equals(EmploymentStatus.SELF_EMPLOYED))
      rate = rate.add(BigDecimal.valueOf(1));
    else if (employment.getEmploymentStatus().equals(EmploymentStatus.BUSINESS_OWNER))
      rate = rate.add(BigDecimal.valueOf(3));

    if (employment.getPosition().equals(Position.MID_MANAGER))
      rate = rate.subtract(BigDecimal.valueOf(2));
    else if (employment.getPosition().equals(Position.TOP_MANAGER))
      rate = rate.subtract(BigDecimal.valueOf(4));

    return rate;
  }

  private BigDecimal getRateBySalary(Boolean isSalaryClient, BigDecimal rate) {
    if (isSalaryClient) rate = rate.subtract(BigDecimal.valueOf(4));
    return rate;
  }

  private BigDecimal getPsk(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
    BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
    BigDecimal amount = scoringDataDTO.getAmount();
    BigDecimal creditCoast =
        (amount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))
            .multiply(monthlyPercent)
            .multiply(BigDecimal.valueOf(scoringDataDTO.getTerm()));
    return amount.add(creditCoast).setScale(2, RoundingMode.HALF_EVEN);
  }

  private BigDecimal getRateByInsurance(Boolean isInsuranceEnabled, BigDecimal rate) {
    if (isInsuranceEnabled) rate = rate.subtract(BigDecimal.valueOf(3));
    return rate;
  }
}
