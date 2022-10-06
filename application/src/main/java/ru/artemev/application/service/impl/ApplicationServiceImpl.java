package ru.artemev.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemev.application.client.DealClient;
import ru.artemev.application.dto.LoanApplicationRequestDTO;
import ru.artemev.application.dto.LoanOfferDTO;
import ru.artemev.application.exception.ValidationException;
import ru.artemev.application.service.ApplicationService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

  @Autowired private DealClient dealClient;

  @Override
  public List<LoanOfferDTO> getLoanOfferDtoList(
      LoanApplicationRequestDTO loanApplicationRequestDTO) {
    /*
    Имя, Фамилия - от 2 до 30 латинских букв. Отчество, при наличии - от 2 до 30 латинских букв.
    Сумма кредита - действительно число, большее или равное 10000.
    Срок кредита - целое число, большее или равное 6.
    Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня.
    Email адрес - строка, подходящая под паттерн [\w\.]{2,50}@[\w\.]{2,20}
    Серия паспорта - 4 цифры, номер паспорта - 6 цифр.
     */
    log.info("Start getLoanOfferDtoList");
    log.info(loanApplicationRequestDTO.toString());
    if (loanApplicationRequestDTO.getFirstName().length() < 2
        || loanApplicationRequestDTO.getFirstName().length() > 30)
      throw new ValidationException("Имя должно быть длинной от 2 до 30 символов");

    if (loanApplicationRequestDTO.getLastName().length() <= 2
        || loanApplicationRequestDTO.getLastName().length() > 30)
      throw new ValidationException("Фамилия должно быть длинной от 2 до 30 символов");

    if (loanApplicationRequestDTO.getMiddleName() != null
        && (loanApplicationRequestDTO.getMiddleName().length() <= 2
            || loanApplicationRequestDTO.getMiddleName().length() > 30))
      throw new ValidationException("Отчество должно быть длинной от 2 до 30 символов");

    if (loanApplicationRequestDTO.getAmount().intValue() < 10_000)
      throw new ValidationException("Сумма кредита меньше 10000");

    if (loanApplicationRequestDTO.getTerm() < 6)
      throw new ValidationException("Срок займа не может быть меньше 6 месяцев");

    if (Period.between(loanApplicationRequestDTO.getBirthday(), LocalDate.now()).getYears() < 18)
      throw new ValidationException("Возраст менее 18");

    if (!Pattern.matches("[\\w.]{2,50}@[\\w.]{2,20}", loanApplicationRequestDTO.getEmail()))
      throw new ValidationException("Email введен некорректно");

    if (!Pattern.matches("[\\d]{4}", loanApplicationRequestDTO.getPassportSeries()))
      throw new ValidationException("Серия паспорта введена некорректно");

    if (!Pattern.matches("[\\d]{6}", loanApplicationRequestDTO.getPassportNumber()))
      throw new ValidationException("Номер паспорта введен некорректно");

    List<LoanOfferDTO> listResponseEntity =
        dealClient.calculationPossibleLoans(loanApplicationRequestDTO);

    log.info("Finish getLoanOfferDtoList");

    return listResponseEntity;
  }

  @Override
  public void selectOneOfOffers(LoanOfferDTO loanOfferDTO) {
    log.info("Start selectOneOfOffers");
    dealClient.selectOneOfOffers(loanOfferDTO);
    log.info("Finish selectOneOfOffers");
  }
}
