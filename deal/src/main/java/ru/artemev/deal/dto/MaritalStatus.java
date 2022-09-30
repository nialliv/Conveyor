package ru.artemev.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Семейное положение")
public enum MaritalStatus {
    @Schema(description = "Замужем/женат")
    MARRIED,
    @Schema(description = "В разводе")
    DIVORCED,
    @Schema(description = "Холост")
    SINGLE,
    @Schema(description = "Вдова")
    WIDOW_WIDOWER
}
