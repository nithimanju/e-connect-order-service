package com.e_connect.order_service.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SALES_ORDER")
public class SalesOrder extends BaseRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ORDER_ID")
  private Long orderId;
  @Column(name = "ORDER_NUMBER")
  private String orderNumber;
  @Column(name = "CART_ID")
  private Long cartId;
  @Column(name = "STATUS_ID")
  private Long statusId;
  @Column(name = "DEALER_ID")
  private Long dealerId;
  @Column(name = "DEALER_ADDRESS_ID")
  private Long dealerAddressId;
  @Column(name = "OWNER_ADDRESS_ID")
  private Long ownerAddressId;
  @Column(name = "PAYMENT_TYPE_ID")
  private Long paymentTypeId;
  @Column(name = "OWNER_ID")
  private Long ownerId;
  @Column(name = "ORDER_DATE")
  private Date orderDate;
  @Column(name = "TOTAL_PAID_AMOUNT")
  private BigDecimal totalPaidAmount;
  @Column(name = "TOTAL_TAX_AMOUNT")
  private BigDecimal totalTaxAmount;
  @Column(name = "TOTAL_FREIGHT_AMOUNT")
  private BigDecimal totalFreightAmount;
  @Column(name = "TOTAL_DISCOUNT_AMOUNT")
  private BigDecimal totalDiscountAmount;
  @Column(name = "ACTIVE")
  private Boolean active;
}
