package com.e_connect.order_service.order.dto;

import com.e_connect.order_service.dto.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderTransactionRequest extends BaseRequest {

  private Long orderId;
  private Long cartId;
  private Long dealerId;
  private Long ownerAddressId;
  private Long delaerAddressId;
  private Long discountCopounId;
  private Long paymentTypeId;
}
