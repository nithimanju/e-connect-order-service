package com.e_connect.order_service.order.dto;

import java.io.Serializable;
import java.util.List;

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
public class OrderReturnListResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  private List<OrderListResponse> orderListResponses;
  private int count;
}
