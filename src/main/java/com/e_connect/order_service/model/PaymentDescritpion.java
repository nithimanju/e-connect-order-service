package com.e_connect.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "PAYMENT_TYPE_DESCRIPTION")
public class PaymentDescritpion extends BaseRecord {

  @EmbeddedId
  private PaymentTypeDescriptionEmbeddedId paymentTypeDescriptionEmbeddedId;

  @Column(name = "PAYMENT_TYPE_DESCRIPTION")
  private String paymentTypeDescription;
  @Column(name = "PAYMENT_TYPE_NAME")
  private String paymentTypeName;
  @ManyToOne
  @JoinColumn(name = "PAYMENT_TYPE_ID", updatable = false, insertable = false)
  private PaymentType paymentType;

  @Embeddable
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  @Builder(toBuilder = true)
  @Getter
  public static class PaymentTypeDescriptionEmbeddedId {
    @Column(name = "LANGUAGE_ID")
    private Long languageId;
    @Column(name = "PAYMENT_TYPE_ID")
    private Long paymentTypeId;
  }
}
