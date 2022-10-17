package ru.artemev.conveyor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
  @ExceptionHandler(value = {BaseException.class})
  public ResponseEntity<ApiError> handlerHandlerException(BaseException ex) {
    ApiError apiError = ex.getApiError();
    HttpStatus httpStatus = ex.getHttpStatus();
    ex.printStackTrace();
    return new ResponseEntity<>(apiError, httpStatus);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ApiError> defaultHandlerException(Exception e) {
    ApiError apiError = new ApiError(e.getClass().getName(), e.getMessage());
    e.printStackTrace();
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }
}
