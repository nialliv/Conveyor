package ru.artemev.gateway.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler(BaseException::class)
    fun handlerHandlerException(ex: BaseException): ResponseEntity<ApiError> {
        return ResponseEntity(ex.apiError, ex.httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun defaultHandlerException(ex: Exception): ResponseEntity<ApiError> {
        val apiError = ApiError(ex.javaClass.name, ex.message.toString())
        return ResponseEntity(apiError, HttpStatus.BAD_REQUEST)
    }
}