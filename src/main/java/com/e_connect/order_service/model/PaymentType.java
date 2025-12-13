package com.e_connect.order_service.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Table(name = "PAYMENT_TYPE")
public class PaymentType extends BaseRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PAYMENT_TYPE_ID")
  private Long paymentTypeId;
  @OneToMany(mappedBy = "paymentType", fetch = FetchType.EAGER)
  private List<PaymentDescritpion> paymentTypeDescriptions;
  @Column(name = "ACTIVE")
  private Boolean isActive;
}
