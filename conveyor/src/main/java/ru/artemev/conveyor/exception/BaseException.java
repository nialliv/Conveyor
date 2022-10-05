package ru.artemev.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BaseException extends RuntimeException {
  private HttpStatus httpStatus;
  private ApiError apiError;
}
