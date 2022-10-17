package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.model.Passport;

@Mapper
public abstract class ClientEntityMapper {

  public static ClientEntity toClientEntity(LoanApplicationRequestDTO loanApplicationRequestDTO) {
    return ClientEntity.builder()
        .firstName(loanApplicationRequestDTO.getFirstName())
        .lastName(loanApplicationRequestDTO.getLastName())
        .middleName(loanApplicationRequestDTO.getMiddleName())
        .email(loanApplicationRequestDTO.getEmail())
        .birthday(loanApplicationRequestDTO.getBirthday())
        .passport(
            Passport.builder()
                .series(loanApplicationRequestDTO.getPassportSeries())
                .number(loanApplicationRequestDTO.getPassportNumber())
                .build())
        .build();
  }

  public static void fieldClientEntity(
      ClientEntity clientEntity, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
    clientEntity.setGender(finishRegistrationRequestDTO.getGender());
    clientEntity.setAccount(finishRegistrationRequestDTO.getAccount());
    clientEntity.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
    clientEntity.setEmployment(finishRegistrationRequestDTO.getEmployment());
    clientEntity.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
    clientEntity.setPassport(
        Passport.builder()
            .number(clientEntity.getPassport().getNumber())
            .series(clientEntity.getPassport().getSeries())
            .issueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
            .issueDate(finishRegistrationRequestDTO.getPassportIssueDate())
            .build());
  }
}
