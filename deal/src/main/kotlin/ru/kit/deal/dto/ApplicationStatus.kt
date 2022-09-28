package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Статус заявки")
enum class ApplicationStatus {
    @Schema(description = "Предодобрена")
    PREAPPROVAL,

    @Schema(description = "Одобрена")
    APPROVED,

    @Schema(description = "Отклонена кредитным конвейером")
    CC_DENIED,

    @Schema(description = "Одобрена кредитным конвейером")
    CC_APPROVED,

    @Schema(description = "Подготовка документов")
    PREPARE_DOCUMENTS,

    @Schema(description = "Документы созданы")
    DOCUMENT_CREATED,

    @Schema(description = "Отклонено Клиентом")
    CLIENT_DENIED,

    @Schema(description = "Документы подписаны")
    DOCUMENT_SIGNED,

    @Schema(description = "Кредит выдан")
    CREDIT_ISSUED,
}