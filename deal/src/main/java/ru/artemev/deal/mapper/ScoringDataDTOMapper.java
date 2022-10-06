package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import ru.artemev.deal.dto.LoanOfferDTO;
import ru.artemev.deal.dto.ScoringDataDTO;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;

@Mapper
public abstract class ScoringDataDTOMapper {
  public static ScoringDataDTO toScoringDataDTO(ApplicationEntity applicationEntity) {
    LoanOfferDTO loanOfferDTO = applicationEntity.getAppliedOffer();
    ClientEntity clientEntity = applicationEntity.getClientEntity();
    return ScoringDataDTO.builder()
        .amount(loanOfferDTO.getTotalAmount())
        .term(loanOfferDTO.getTerm())
        .firstName(clientEntity.getFirstName())
        .lastName(clientEntity.getLastName())
        .middleName(clientEntity.getMiddleName())
        .gender(clientEntity.getGender())
        .birthday(clientEntity.getBirthday())
        .passportSeries(clientEntity.getPassport().getSeries())
        .passportNumber(clientEntity.getPassport().getNumber())
        .passportIssueBranch(clientEntity.getPassport().getIssueBranch())
        .passportIssueDate(clientEntity.getPassport().getIssueDate())
        .maritalStatus(clientEntity.getMaritalStatus())
        .dependentAmount(clientEntity.getDependentAmount())
        .employment(clientEntity.getEmployment())
        .account(clientEntity.getAccount())
        .insuranceEnabled(loanOfferDTO.isInsuranceEnabled())
        .salaryClient(loanOfferDTO.isSalaryClient())
        .build();
  }
}
