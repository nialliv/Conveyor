package ru.artemev.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.artemev.deal.dto.EmploymentDTO;
import ru.artemev.deal.dto.Gender;
import ru.artemev.deal.dto.MaritalStatus;
import ru.artemev.deal.dto.Passport;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "middle_name", nullable = false)
  private String middleName;

  @Column(name = "birthday", nullable = false)
  private LocalDate birthday;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "marital_status")
  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  @Column(name = "dependent_amount")
  private Integer dependentAmount;

  @Type(type = "jsonb")
  @Column(name = "passport", columnDefinition = "jsonb")
  private Passport passport;

  @Type(type = "jsonb")
  @Column(name = "employment", columnDefinition = "jsonb")
  private EmploymentDTO employment;

  @Column(name = "account")
  private String account;

  @OneToMany(mappedBy = "clientEntity")
  private List<ApplicationEntity> applicationEntities;
}
