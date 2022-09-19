package ru.artemev.conveyor.exception;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
