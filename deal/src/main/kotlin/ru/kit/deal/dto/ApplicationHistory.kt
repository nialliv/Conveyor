package ru.kit.deal.dto

import java.time.LocalDate

data class ApplicationHistory(
    private val status: ApplicationStatus,
    private val date: LocalDate,
)
