package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.artemev.deal.dto.CreditDTO;
import ru.artemev.deal.entity.CreditEntity;

@Mapper(componentModel = "spring")
public interface CreditEntityMapper {

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "creditStatus", ignore = true),
    @Mapping(target = "applicationEntity", ignore = true)
  })
  CreditEntity toCreditEntity(CreditDTO creditDTO);
}
