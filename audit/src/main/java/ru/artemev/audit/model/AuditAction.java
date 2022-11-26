package ru.artemev.audit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.artemev.audit.model.enums.AuditService;
import ru.artemev.audit.model.enums.AuditType;

import java.util.UUID;

/*
uuid: UUID,
type: Enum[START,SUCCESS,FAILURE]
service: Enum[APPLICATION,DEAL,CONVEYOR,DOSSIER]
message: String
 */

@Data
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = AuditAction.AuditActionBuilder.class)
public class AuditAction {
  @JsonProperty("uuid")
  private UUID uuid;

  @JsonProperty("type")
  private AuditType type;

  @JsonProperty("service")
  private AuditService service;

  @JsonProperty("message")
  private String message;
}
