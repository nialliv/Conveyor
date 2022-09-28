package ru.kit.deal.dto

import java.time.LocalDate

data class ApplicationHistory(
    val status: ApplicationStatus,
    val date: LocalDate,
)
