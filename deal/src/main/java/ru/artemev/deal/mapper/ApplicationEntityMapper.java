package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;

@Mapper(componentModel = "spring")
public interface ApplicationEntityMapper {

  @Mappings({
    @Mapping(target = "creditEntity", ignore = true),
    @Mapping(target = "applicationStatus", ignore = true),
    @Mapping(target = "appliedOffer", ignore = true),
    @Mapping(target = "signDate", ignore = true),
    @Mapping(target = "sesCode", ignore = true),
    @Mapping(target = "statusHistory", ignore = true),
    @Mapping(target = "creationDate", ignore = true),
    @Mapping(target = "clientEntity", source = "clientEntity")
  })
  ApplicationEntity toApplicationEntity(ClientEntity clientEntity);
}
