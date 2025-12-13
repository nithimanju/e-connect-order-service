package com.e_connect.order_service.payment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentType implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long paymentTypeId;
  private String paymentTypeDescription;
  private String paymentTypeName;
  private Long languageId;
}
