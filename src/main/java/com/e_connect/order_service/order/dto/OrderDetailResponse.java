package com.e_connect.order_service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.e_connect.order_service.dto.CartItemDetailResponse;

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
public class OrderDetailResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String orderNumber;
  private Long orderId;
  private Long dealerId;
  private String dealerName;
  private BigDecimal subTotal;
  private BigDecimal totalPaidAmount;
  private BigDecimal totalTaxAmount;
  private BigDecimal totalFreightAmount;
  private BigDecimal totalDiscountAmount;
  private String statusName;
  private String paymentTypeName;
  private Date orderedDate;
  private Long cartId;
  private List<CartItemDetailResponse> cartItemDetailResponses;
}
