package ru.artemev.conveyor.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
  public ValidationException(String error) {
    super(HttpStatus.NOT_FOUND, new ApiError("Validation Exception", error));
  }
}
