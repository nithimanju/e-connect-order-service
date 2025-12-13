package com.e_connect.order_service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderListResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long orderId;
  private BigDecimal totalPrice;
  private String statusName;
  private String orderNumber;
  private String paymentTypeName;
  private Date orderedDate;
}
