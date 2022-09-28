package ru.kit.deal.repository

import org.springframework.data.repository.CrudRepository
import ru.kit.deal.entity.Application

interface ApplicationRepository : CrudRepository<Application, Long>