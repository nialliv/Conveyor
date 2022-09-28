package ru.kit.deal.repository

import org.springframework.data.repository.CrudRepository
import ru.kit.deal.entity.Client

interface ClientRepository : CrudRepository<Client, Long>