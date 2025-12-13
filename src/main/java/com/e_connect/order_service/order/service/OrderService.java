package com.e_connect.order_service.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.e_connect.order_service.dto.CartDetailResponse;
import com.e_connect.order_service.dto.CartRequest;
import com.e_connect.order_service.feign.cart.CartProxy;
import com.e_connect.order_service.model.SalesOrder;
import com.e_connect.order_service.order.dto.OrderDetailResponse;
import com.e_connect.order_service.order.dto.OrderFetchRequest;
import com.e_connect.order_service.order.dto.OrderListResponse;
import com.e_connect.order_service.order.dto.OrderReturnListResponse;
import com.e_connect.order_service.order.dto.OrderTransactionRequest;
import com.e_connect.order_service.order.repository.SalesOrderRepository;
import com.e_connect.order_service.payment.dto.PaymentType;
import com.e_connect.order_service.payment.service.PaymentService;
import com.e_connect.order_service.status.dto.Status;
import com.e_connect.order_service.status.service.StatusService;
import com.e_connect.order_service.utils.PredicateUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

  private final SalesOrderRepository salesOrderRepository;
  private final CartProxy cartProxy;
  private final PaymentService paymentService;
  private final StatusService statusService;
  private final PredicateUtilsService<SalesOrder> predicateUtilsService;

  public OrderDetailResponse get(OrderFetchRequest orderRequest) throws Exception {
    OrderDetailResponse orderDetailResponse = null;
    Long languageId = orderRequest.getLanguageId();
    String orderNumber = orderRequest.getOrderNumber();
    Long orderId = orderRequest.getOrderId();

    if (StringUtils.isBlank(orderNumber) && ObjectUtils.isEmpty(orderId)) {
      throw new Exception("Order Number and Order Id is missing in the request");
    }

    Example<SalesOrder> salesOrderexample = Example
        .of(SalesOrder.builder().active(true).ownerId(orderRequest.getUserId()).build());
    Specification<SalesOrder> salesOrderSpecification = getOrderPredicate(orderRequest, salesOrderexample);
    List<SalesOrder> salesOrders = salesOrderRepository.findAll(salesOrderSpecification);

    if (ObjectUtils.isEmpty(salesOrders)) {
      return null;
    }

    SalesOrder salesOrder = salesOrders.getFirst();
    orderDetailResponse = buildOrderDetailResponse(salesOrder, languageId);

    CartDetailResponse cartDetailResponse = cartProxy.get(salesOrder.getCartId(), orderRequest.getUserId(), getCartStatusIds());
    if (ObjectUtils.isNotEmpty(cartDetailResponse)) {
      orderDetailResponse = orderDetailResponse.toBuilder()
          .cartItemDetailResponses(cartDetailResponse.getCartItems()).build();
    }

    return orderDetailResponse;
  }

  private Specification<SalesOrder> getOrderPredicate(OrderFetchRequest orderRequest,
      Example<SalesOrder> salesOrderexample) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates = predicateUtilsService.andValidePredicate(predicates, root, "orderId", orderRequest.getOrderId());

      predicates = predicateUtilsService.andValidePredicate(predicates, root, "orderNumber",
          orderRequest.getOrderNumber());

      predicates = predicateUtilsService.andValidePredicate(predicates, root, "statusId", orderRequest.getStatusId());

      predicates = predicateUtilsService.andValidePredicate(predicates, root, "paymentTypeId",
          orderRequest.getPaymentTypeId());

      if (ObjectUtils.isNotEmpty(orderRequest.getFromDate())) {
        builder.greaterThan(root.get("orderDate"), orderRequest.getFromDate());
      }
      if (ObjectUtils.isNotEmpty(orderRequest.getToDate())) {
        builder.lessThan(root.get("orderDate"), orderRequest.getToDate());
      }

      predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, salesOrderexample));
      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  public OrderReturnListResponse getAll(OrderFetchRequest orderRequest) {
    OrderReturnListResponse orderReturnListResponse = null;
    Long languageId = orderRequest.getLanguageId();

    Example<SalesOrder> salesOrderexample = Example
        .of(SalesOrder.builder().active(true).ownerId(orderRequest.getUserId()).build());
    Specification<SalesOrder> salesOrderSpecification = getOrderPredicate(orderRequest, salesOrderexample);
    List<SalesOrder> salesOrders = salesOrderRepository.findAll(salesOrderSpecification);

    if (ObjectUtils.isEmpty(salesOrders)) {
      return null;
    }

    List<OrderListResponse> orderListResponses = salesOrders.stream()
        .map(order -> buildOrderListResponse(order, languageId)).toList();
    orderReturnListResponse = OrderReturnListResponse.builder().count(orderListResponses.size())
        .orderListResponses(orderListResponses).build();

    return orderReturnListResponse;
  }

  public OrderDetailResponse post(OrderTransactionRequest orderRequest) throws Exception {

    CartDetailResponse cartDetailResponse = cartProxy.get(orderRequest.getCartId(), orderRequest.getUserId(), List.of(3L));
    if (ObjectUtils.isEmpty(cartDetailResponse)) {
      throw new Exception("Cart: " + orderRequest.getCartId() + " not found for user: " + orderRequest.getUserId());
    }

    OrderDetailResponse orderDetailResponse = null;
    SalesOrder salesOrder = buildSalesOrder(cartDetailResponse, orderRequest);

    salesOrderRepository.save(salesOrder);

    orderDetailResponse = buildOrderDetailResponse(salesOrder, orderRequest.getLanguageId());
    orderDetailResponse = orderDetailResponse.toBuilder()
        .cartItemDetailResponses(cartDetailResponse.getCartItems()).build();
    return orderDetailResponse;
  }

  private SalesOrder buildSalesOrder(CartDetailResponse cartDetailResponse, OrderTransactionRequest orderRequest) {
    SalesOrder salesOrder = SalesOrder.builder()
        .orderNumber("")
        .cartId(cartDetailResponse.getCartId())
        .statusId(1L)
        .dealerId(orderRequest.getDealerId())
        .dealerAddressId(orderRequest.getDelaerAddressId())
        .ownerAddressId(orderRequest.getOwnerAddressId())
        .paymentTypeId(orderRequest.getPaymentTypeId())
        .ownerId(orderRequest.getUserId())
        .orderDate(new Date())
        .totalPaidAmount(cartDetailResponse.getTotalAmount())
        .totalTaxAmount(cartDetailResponse.getTotalTaxAmount())
        .active(true)
        .build();
    return salesOrder;
  }

  private OrderListResponse buildOrderListResponse(SalesOrder salesOrder, Long languageId) {
    OrderListResponse orderListResponse = OrderListResponse.builder()
        .orderId(salesOrder.getOrderId())
        .totalPrice(salesOrder.getTotalPaidAmount())
        .statusName(populateStatus(salesOrder, languageId))
        .orderNumber(salesOrder.getOrderNumber())
        .paymentTypeName(populatePaymentDescription(salesOrder, languageId))
        .orderedDate(salesOrder.getOrderDate())
        .build();
    return orderListResponse;
  }

  private OrderDetailResponse buildOrderDetailResponse(SalesOrder salesOrder, Long languageId) {
    
    return OrderDetailResponse.builder()
        .orderId(salesOrder.getOrderId())
        .totalPaidAmount(salesOrder.getTotalPaidAmount())
        .statusName(populateStatus(salesOrder, languageId))
        .orderNumber(salesOrder.getOrderNumber())
        .paymentTypeName(populatePaymentDescription(salesOrder, languageId))
        .orderedDate(salesOrder.getOrderDate())
        .dealerId(salesOrder.getDealerId())
        .totalTaxAmount(salesOrder.getTotalTaxAmount())
        .totalFreightAmount(salesOrder.getTotalTaxAmount())
        .totalDiscountAmount(salesOrder.getTotalDiscountAmount()).build();
  }

  private String populatePaymentDescription(SalesOrder salesOrder,
      Long languageId) {
    Long paymentTypeId = salesOrder.getPaymentTypeId();
    PaymentType paymentType = paymentService.get(paymentTypeId, languageId);
    return paymentType.getPaymentTypeName();
  }

  private String populateStatus(SalesOrder salesOrder,
      Long languageId) {
    Long statusId = salesOrder.getStatusId();
    Status status = statusService.get(statusId, languageId);
    return status.getStatusName();
  }

  public void cleareExistingCart(CartRequest cartRequest) throws Exception {
    if(ObjectUtils.isEmpty(cartProxy.put(cartRequest.toBuilder().cartStatusIds(getCartStatusIds()).build()))){
      throw new Exception("Not able to clear the Cart after creating Order");
    }
  }
  private List<Long> getCartStatusIds() {
    return List.of(1L);
  }
}
