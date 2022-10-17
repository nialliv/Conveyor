package ru.artemev.deal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artemev.deal.entity.CreditEntity;

public interface CreditRepository extends CrudRepository<CreditEntity, Long> {}
