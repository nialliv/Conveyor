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
    val id: Long? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String? = null,

    @Column(name = "last_name", nullable = false)
    val lastName: String? = null,

    @Column(name = "middle_name", nullable = false)
    val middleName: String? = null,

    @Column(name = "birthday", nullable = false)
    val birthday: LocalDate? = null,

    @Column(name = "email", nullable = false)
    val email: String? = null,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    val gender: Gender? = null,

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    val maritalStatus: MaritalStatus? = null,

    @Column(name = "dependent_amount")
    val dependentAmount: Int? = null,

    @Type(type = "jsonb")
    @Column(name = "passport", columnDefinition = "jsonb")
    val passport: Passport? = null,

    @Type(type = "jsonb")
    @Column(name = "employment", columnDefinition = "jsonb")
    val employment: EmploymentDTO? = null,

    @Column(name = "account")
    val account: String? = null,

    @OneToMany(mappedBy = "client")
    val applications: List<Application>? = null,
)
