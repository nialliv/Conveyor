package ru.kit.deal.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import lombok.Builder
import lombok.Data
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import ru.kit.deal.dto.EmploymentDTO
import ru.kit.deal.dto.Gender
import ru.kit.deal.dto.MaritalStatus
import ru.kit.deal.dto.Passport
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Builder
@Data
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,

    @Column(name = "first_name", nullable = false)
    private val firstName: String? = null,

    @Column(name = "last_name", nullable = false)
    private val lastName: String? = null,

    @Column(name = "middle_name", nullable = false)
    private val middleName: String? = null,

    @Column(name = "birthday", nullable = false)
    private val birthday: LocalDate? = null,

    @Column(name = "email", nullable = false)
    private val email: String? = null,

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private val gender: Gender? = null,

    @Column(name = "marital_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private val maritalStatus: MaritalStatus? = null,

    @Column(name = "dependent_amount", nullable = false)
    private val dependentAmount: Int? = null,

    @Type(type = "jsonb")
    @Column(name = "passport", columnDefinition = "jsonb")
    private val passport: Passport? = null,

    @Type(type = "jsonb")
    @Column(name = "employment", columnDefinition = "jsonb")
    private val employment: EmploymentDTO? = null,

    @Column(name = "account")
    private val account: String? = null,

    @OneToMany(mappedBy = "client")
    private val applications: List<Application>? = null,
)
