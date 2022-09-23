package ru.kit.deal.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

@Data
@Schema(description = "Данные для оценки")
class ScoringDataDTO {
    @NotNull
    @DecimalMin(value = "100000", message = "Сумма не может быть меньше 100 000р")
    @Schema(description = "Сумма кредита", example = "100000")
    private val amount: BigDecimal? = null

    @Min(value = 6, message = "Срок займа не может быть меньше 6 месяцев")
    @NotNull
    @Schema(description = "Срок займа", example = "12")
    private val term: Int? = null

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Имя", example = "Петя")
    private val firstName: String? = null

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Фамилия", example = "Петров")
    private val lastName: String? = null

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Отчество", example = "Петрович")
    private val middleName: String? = null

    @NotBlank
    @Schema(description = "Пол", example = "MALE")
    private val gender: Gender? = null

    @NotNull
    @Past(message = "Дата может быть только прошлым")
    private val birthday: LocalDate? = null

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Не верно введена серия паспорта")
    @Schema(description = "Серия паспорта", example = "1234")
    private val passportSeries: String? = null

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Не верно введен номер паспорта")
    @Schema(description = "Номер пасорта", example = "567890")
    private val passportNumber: String? = null

    @NotBlank
    @Past
    @Schema(description = "Дата выдачи паспорта", example = "2000-01-01")
    private val passportIssueDate: LocalDate? = null

    @NotBlank
    @Schema(description = "Кем выдан паспорт", example = "Moscow")
    private val passportIssueBranch: String? = null

    @NotBlank
    @Schema(description = "Семейное положение", example = "SINGLE")
    private val maritalStatus: MaritalStatus? = null

    @NotNull
    @Schema(description = "Количество иждивенцев", example = "1")
    private val dependentAmount: Int? = null

    @NotNull
    @Schema(description = "Трудоустройство")
    private val employment: EmploymentDTO? = null

    @NotNull
    @Schema(description = "Учетная запись")
    private val account: String? = null

    @NotNull
    @Schema(description = "Страховка", example = "true")
    private val isInsuranceEnabled: Boolean? = null

    @NotNull
    @Schema(description = "Зарплатный клиент", example = "true")
    private val isSalaryClient: Boolean? = null
}