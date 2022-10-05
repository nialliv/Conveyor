package ru.artemev.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.artemev.deal.model.enums.CreditStatus;
import ru.artemev.deal.model.PaymentScheduleElement;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "credit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "term", nullable = false)
  private Integer term;

  @Column(name = "monthly_payment", nullable = false)
  private BigDecimal monthlyPayment;

  @Column(name = "rate", nullable = false)
  private BigDecimal rate;

  @Column(name = "psk", nullable = false)
  private BigDecimal psk;

  @Column(name = "payment_schedule", columnDefinition = "jsonb", nullable = false)
  @Type(type = "jsonb")
  private List<PaymentScheduleElement> paymentSchedule;

  @Column(name = "is_insurance_enabled", nullable = false)
  private Boolean insuranceEnabled;

  @Column(name = "is_salary_client", nullable = false)
  private Boolean salaryClient;

  @Column(name = "credit_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private CreditStatus creditStatus;

  @OneToOne(mappedBy = "creditEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ApplicationEntity applicationEntity;
}
