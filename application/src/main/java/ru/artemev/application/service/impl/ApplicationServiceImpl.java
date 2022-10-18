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

    if (Period.between(loanApplicationRequestDTO.getBirthday(), LocalDate.now()).getYears() < 18)
      throw new ValidationException("Возраст менее 18");

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
