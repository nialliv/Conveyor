package ru.artemev.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.artemev.deal.entity.CreditEntity;

@Repository
public interface CreditRepository extends CrudRepository<CreditEntity, Long> {}
