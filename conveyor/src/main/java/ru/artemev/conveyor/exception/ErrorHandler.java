package ru.artemev.conveyor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ApiError> handlerHandlerException(BaseException ex) {
    return ResponseEntity.status(ex.getHttpStatus()).body(ex.getApiError());
  }
}
