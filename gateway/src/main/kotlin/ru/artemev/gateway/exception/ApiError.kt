package ru.artemev.gateway.exception

data class ApiError(
    val errorCode: String,
    val description: String
)