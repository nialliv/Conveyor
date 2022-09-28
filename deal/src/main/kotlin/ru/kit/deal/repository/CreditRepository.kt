package ru.kit.deal.repository

import org.springframework.data.repository.CrudRepository
import ru.kit.deal.entity.Credit

interface CreditRepository : CrudRepository<Credit, Long>