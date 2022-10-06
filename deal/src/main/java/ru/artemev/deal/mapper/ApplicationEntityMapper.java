package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import ru.artemev.deal.entity.ApplicationEntity;
import ru.artemev.deal.entity.ClientEntity;

import java.time.LocalDate;

@Mapper
public abstract class ApplicationEntityMapper {
  public static ApplicationEntity toApplicationEntity(ClientEntity clientEntity) {
    return ApplicationEntity.builder()
        .clientEntity(clientEntity)
        .creationDate(LocalDate.now())
        .build();
  }
}
