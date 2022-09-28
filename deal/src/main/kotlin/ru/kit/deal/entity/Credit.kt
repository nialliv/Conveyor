package ru.kit.deal.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import lombok.Data
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import ru.kit.deal.dto.CreditStatus
import ru.kit.deal.dto.PaymentScheduleElement
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "credit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Data
class Credit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "term", nullable = false)
    val term: Int,

    @Column(name = "monthly_payment", nullable = false)
    val monthlyPayment: BigDecimal,

    @Column(name = "rate", nullable = false)
    val rate: BigDecimal,

    @Column(name = "psk", nullable = false)
    val psk: BigDecimal,

    @Column(name = "payment_schedule", columnDefinition = "jsonb", nullable = false)
    @Type(type = "jsonb")
    val paymentSchedule: List<PaymentScheduleElement>? = null,

    @Column(name = "is_insurance_enabled", nullable = false)
    val isInsuranceEnabled: Boolean,

    @Column(name = "is_salary_client", nullable = false)
    val isSalaryClient: Boolean,

    @Column(name = "credit_status", nullable = false)
    @Enumerated(EnumType.STRING)
    val creditStatus: CreditStatus? = null,

    @OneToOne(mappedBy = "credit")
    val application: Application,
)