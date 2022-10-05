package ru.artemev.deal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
  private String errorCode;
  private String description;
}
