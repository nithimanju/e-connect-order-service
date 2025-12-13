package com.e_connect.order_service.order.dto;

import java.util.Date;

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
public class OrderFetchRequest extends BaseRequest {

  private Long orderId;
  private String orderNumber;
  private int from;
  private int size;
  private Date fromDate;
  private Date toDate;
  private String paymentDescriptionName;
  private String statusName;
  private String paymentTypeId;
  private String statusId;
}
