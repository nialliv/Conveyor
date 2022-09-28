package ru.kit.deal.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import lombok.Data
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import ru.kit.deal.dto.ApplicationHistory
import ru.kit.deal.dto.ApplicationStatus
import ru.kit.deal.dto.LoanOfferDTO
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "application")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Data
class Application(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne()
    @JoinColumn(name = "client_id", nullable = false)
    val client: Client? = null,

    @OneToOne()
    @JoinColumn(name = "credit_id")
    val credit: Credit? = null,

    @Column(name = "application_status")
    @Enumerated(EnumType.STRING)
    val applicationStatus: ApplicationStatus? = null,

    @Column(name = "creation_date", nullable = false)
    val creationDate: LocalDate? = null,

    @Column(name = "applied_offer", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    val appliedOffer: LoanOfferDTO? = null,

    @Column(name = "sign_date")
    val signDate: LocalDate? = null,

    @Column(name = "ses_code")
    val sesCode: String? = null,

    @Column(name = "status_history", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    var statusHistory: List<ApplicationHistory>? = null,
)