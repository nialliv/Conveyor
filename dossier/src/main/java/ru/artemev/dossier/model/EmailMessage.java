package ru.artemev.dossier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.artemev.dossier.model.enums.Theme;

@Data
@AllArgsConstructor
@Schema(description = "Email сообщение")
@Builder
@JsonDeserialize(builder = EmailMessage.EmailMessageBuilder.class)
public class EmailMessage {

  @Schema(description = "Адрес получателя")
  @JsonProperty("address")
  private String address;

  @Schema(description = "Тема письма")
  @JsonProperty("theme")
  private Theme theme;

  @Schema(description = "Номер заявки")
  @JsonProperty("applicationId")
  private Long applicationId;
}
