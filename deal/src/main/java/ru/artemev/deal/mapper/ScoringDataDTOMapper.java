package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.artemev.deal.dto.ScoringDataDTO;
import ru.artemev.deal.entity.ApplicationEntity;

@Mapper(componentModel = "spring")
public interface ScoringDataDTOMapper {
  @Mappings({
    @Mapping(target = "amount", source = "applicationEntity.appliedOffer.totalAmount"),
    @Mapping(target = "term", source = "applicationEntity.appliedOffer.term"),
    @Mapping(target = "firstName", source = "applicationEntity.clientEntity.firstName"),
    @Mapping(target = "lastName", source = "applicationEntity.clientEntity.lastName"),
    @Mapping(target = "middleName", source = "applicationEntity.clientEntity.middleName"),
    @Mapping(target = "gender", source = "applicationEntity.clientEntity.gender"),
    @Mapping(target = "birthday", source = "applicationEntity.clientEntity.birthday"),
    @Mapping(target = "passportSeries", source = "applicationEntity.clientEntity.passport.series"),
    @Mapping(target = "passportNumber", source = "applicationEntity.clientEntity.passport.number"),
    @Mapping(
        target = "passportIssueBranch",
        source = "applicationEntity.clientEntity.passport.issueBranch"),
    @Mapping(
        target = "passportIssueDate",
        source = "applicationEntity.clientEntity.passport.issueDate"),
    @Mapping(target = "maritalStatus", source = "applicationEntity.clientEntity.maritalStatus"),
    @Mapping(target = "dependentAmount", source = "applicationEntity.clientEntity.dependentAmount"),
    @Mapping(target = "employment", source = "applicationEntity.clientEntity.employment"),
    @Mapping(target = "account", source = "applicationEntity.clientEntity.account"),
    @Mapping(
        target = "isInsuranceEnabled",
        source = "applicationEntity.appliedOffer.insuranceEnabled"),
    @Mapping(target = "isSalaryClient", source = "applicationEntity.appliedOffer.insuranceEnabled")
  })
  ScoringDataDTO toScoringDataDTO(ApplicationEntity applicationEntity);
}
