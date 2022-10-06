package ru.artemev.deal.model;

import lombok.Builder;
import lombok.Data;
import ru.artemev.deal.model.enums.ApplicationStatus;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class ApplicationHistory implements Serializable {
  private ApplicationStatus status;
  private LocalDate date;
}
