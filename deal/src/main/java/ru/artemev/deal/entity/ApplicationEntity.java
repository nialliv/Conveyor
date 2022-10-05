package ru.artemev.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.artemev.deal.model.ApplicationHistory;
import ru.artemev.deal.model.enums.ApplicationStatus;
import ru.artemev.deal.dto.LoanOfferDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "application")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "client_id", nullable = false)
  private ClientEntity clientEntity;

  @OneToOne()
  @JoinColumn(name = "credit_id")
  private CreditEntity creditEntity;

  @Column(name = "application_status")
  @Enumerated(EnumType.STRING)
  private ApplicationStatus applicationStatus;

  @Column(name = "creation_date", nullable = false)
  private LocalDate creationDate;

  @Column(name = "applied_offer", columnDefinition = "jsonb")
  @Type(type = "jsonb")
  private LoanOfferDTO appliedOffer;

  @Column(name = "sign_date")
  private LocalDate signDate;

  @Column(name = "ses_code")
  private String sesCode;

  @Column(name = "status_history", columnDefinition = "jsonb")
  @Type(type = "jsonb")
  private List<ApplicationHistory> statusHistory;
}
