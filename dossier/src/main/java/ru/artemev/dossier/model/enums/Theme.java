package ru.artemev.dossier.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(description = "Тема email сообщения")
public enum Theme {
  CREATE_DOCUMENTS("Создание документов"),
  FINISH_REGISTRATION("Завершение регистрации"),
  SEND_DOCUMENTS("Ваши документы по кредиту"),
  SIGN_DOCUMENTS("Подпишите документы ses-кодом"),
  CREDIT_ISSUED("Кредит одобрен"),
  APPLICATION_DENIED("Заявка отклонена");

  private final String description;
}
