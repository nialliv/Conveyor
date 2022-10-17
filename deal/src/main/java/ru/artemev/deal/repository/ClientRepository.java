package ru.artemev.deal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.artemev.deal.entity.ClientEntity;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {}
