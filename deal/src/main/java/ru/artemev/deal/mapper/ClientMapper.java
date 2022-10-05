package ru.artemev.deal.mapper;

import org.mapstruct.Mapper;
import ru.artemev.deal.dto.LoanApplicationRequestDTO;
import ru.artemev.deal.entity.ClientEntity;
import ru.artemev.deal.model.Passport;

@Mapper
public abstract class ClientMapper {

    public static ClientEntity toClientEntity(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ClientEntity.builder()
                .firstName(loanApplicationRequestDTO.getFirstName())
                .lastName(loanApplicationRequestDTO.getLastName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .email(loanApplicationRequestDTO.getEmail())
                .birthday(loanApplicationRequestDTO.getBirthday())
                .passport(Passport.builder()
                        .series(loanApplicationRequestDTO.getPassportSeries())
                        .number(loanApplicationRequestDTO.getPassportNumber())
                        .build())
                .build();
    }
}
