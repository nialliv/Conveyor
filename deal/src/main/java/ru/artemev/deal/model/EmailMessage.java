package ru.artemev.deal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.artemev.deal.model.enums.Theme;

@Data
@AllArgsConstructor
@Schema(description = "Email сообщение")
public class EmailMessage {

  @Schema(description = "Адрес получателя")
  private String address;

  @Schema(description = "Тема письма")
  private Theme theme;

  @Schema(description = "Номер заявки")
  private Long applicationId;
}
