package ru.artemev.dossier.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(description = "Статус заявки")
public enum ApplicationStatus {
  PREAPPROVAL("Предодобрена"),
  APPROVED("Одобрена"),
  CC_DENIED("Отклонена кредитным конвейером"),
  CC_APPROVED("Одобрена кредитным конвейером"),
  PREPARE_DOCUMENTS("Подготовка документов"),
  DOCUMENT_CREATED("Документы созданы"),
  CLIENT_DENIED("Отклонено Клиентом"),
  DOCUMENT_SIGNED("Документы подписаны"),
  CREDIT_ISSUED("Кредит выдан");

  private final String description;
}
