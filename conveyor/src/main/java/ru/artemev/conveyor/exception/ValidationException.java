package ru.artemev.conveyor.exception;

<<<<<<< HEAD
public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
=======
import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
  public ValidationException(String error) {
    super(HttpStatus.NOT_FOUND, new ApiError("Validation Exception", error));
>>>>>>> feature/MS-conveyor/mvp1
  }
}
