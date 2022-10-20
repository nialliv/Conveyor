package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.artemev.deal.dto.FinishRegistrationRequestDTO;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.entity.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper {
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "gender", ignore = true),
    @Mapping(target = "maritalStatus", ignore = true),
    @Mapping(target = "dependentAmount", ignore = true),
    @Mapping(target = "employment", ignore = true),
    @Mapping(target = "account", ignore = true),
    @Mapping(target = "applicationEntities", ignore = true),
    @Mapping(target = "passport.series", source = "passportSeries"),
    @Mapping(target = "passport.number", source = "passportNumber")
  })
  ClientEntity toClientEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "firstName", ignore = true),
    @Mapping(target = "lastName", ignore = true),
    @Mapping(target = "middleName", ignore = true),
    @Mapping(target = "birthday", ignore = true),
    @Mapping(target = "email", ignore = true),
    @Mapping(target = "applicationEntities", ignore = true),
    @Mapping(target = "passport.issueDate", source = "passportIssueDate"),
    @Mapping(target = "passport.issueBranch", source = "passportIssueBranch")
  })
  void update(
      @MappingTarget ClientEntity clientEntity,
      FinishRegistrationRequestDTO finishRegistrationRequestDTO);
}
