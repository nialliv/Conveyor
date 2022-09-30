package ru.artemev.deal.dto;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class ApplicationHistory implements Serializable {
  private ApplicationStatus status;
  private LocalDate date;
}
