package ru.artemev.conveyor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ConveyorServiceImp implements IConveyorService {

    @Value("${rate.base}")
    private int baseRate;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Start calculated offers");
        List<LoanOfferDTO> loanOfferDTOList = Stream.of(
                getCompletedOffers(1L, false, false, loanApplicationRequestDTO),
                getCompletedOffers(2L, false, true, loanApplicationRequestDTO),
                getCompletedOffers(3L, true, false, loanApplicationRequestDTO),
                getCompletedOffers(4L, true, true, loanApplicationRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
        log.info("Finish calculated offers");
        return loanOfferDTOList;
    }

    @Override
    public CreditDTO getCreditDto(ScoringDataDTO scoringDataDTO) {
        // TODO Auto-generated method stub
        return null;
    }


    private LoanOfferDTO getCompletedOffers(Long applicationId,
                                       boolean isInsuranceEnabled,
                                       boolean isSalaryClient,
                                        LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Complete offer calculation started");
        if(Period.between(loanApplicationRequestDTO.getBirthday(), LocalDate.now()).getYears() < 18)
            throw new ValidationException("Человеку меньше 18 лет");
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        BigDecimal requestedAmount = loanApplicationRequestDTO.getAmount();
        Integer term = loanApplicationRequestDTO.getTerm();

        if(isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }

        if(isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(4));
        }

        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
        BigDecimal creditCoast = (requestedAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))
                .multiply(monthlyPercent)
                .multiply(BigDecimal.valueOf(term));
        BigDecimal totalAmount = requestedAmount.add(creditCoast);
        BigDecimal monthlyPayment = totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);

        LoanOfferDTO loanOfferDTO = LoanOfferDTO
                .builder()
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
}