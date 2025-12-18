package com.e_connect.order_service.order.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_connect.order_service.dto.CartRequest;
import com.e_connect.order_service.order.dto.OrderDetailResponse;
import com.e_connect.order_service.order.dto.OrderFetchRequest;
import com.e_connect.order_service.order.dto.OrderReturnListResponse;
import com.e_connect.order_service.order.dto.OrderTransactionRequest;
import com.e_connect.order_service.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/order")
  @Secured(value = "ROLE_LOGGED_IN")
  public ResponseEntity<OrderDetailResponse> get(@RequestParam(required = false) Long orderId,
      @RequestParam Long userId, @RequestParam Long languageId) {
    log.debug("Request for fetching Order-detail for Order id: {}", orderId, userId);
    OrderFetchRequest orderRequest = OrderFetchRequest.builder().orderId(orderId).userId(userId).build();
    OrderDetailResponse orderDetailResponse = null;
    try {
      orderDetailResponse = orderService.get(orderRequest);
      if (ObjectUtils.isEmpty(orderDetailResponse)) {
        log.warn("No Order detail found for requested Order-id : {}", orderId, userId);
        return new ResponseEntity<>(orderDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching Order-detail for Order id: {}", orderId, userId, e);
      return new ResponseEntity<>(orderDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Order detail {} for requested orderId: {}", orderDetailResponse, orderId, userId);
    return new ResponseEntity<>(orderDetailResponse, HttpStatus.OK);
  }

  @PostMapping("/order")
  @Secured(value = "ROLE_LOGGED_IN")
  public ResponseEntity<OrderDetailResponse> post(@RequestBody OrderTransactionRequest orderRequest, Long userId) {
    log.debug("Request for creating new Order for user: {}", orderRequest.getUserId());
    OrderDetailResponse orderResponse = null;
    orderRequest = orderRequest.toBuilder().userId(userId).build();
    if (ObjectUtils.isEmpty(userId)) {
      log.error("Required Parameters missing in the request: {}", orderRequest);
      return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      orderResponse = orderService.post(orderRequest);
      if (ObjectUtils.isEmpty(orderResponse)) {
        log.warn("No Order has been created : {}", orderRequest.getUserId());
        return new ResponseEntity<>(orderResponse, HttpStatus.NOT_FOUND);
      }
      orderService.cleareExistingCart(CartRequest.builder().cartId(orderRequest.getCartId())
          .userId(userId).build());
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", orderRequest.getUserId(), e);
      return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", orderResponse, orderRequest.getUserId());
    return new ResponseEntity<>(orderResponse, HttpStatus.OK);
  }

  @PostMapping("/order-list")
  @Secured(value = "ROLE_LOGGED_IN")
  public ResponseEntity<OrderReturnListResponse> getList(
      @RequestBody OrderFetchRequest orderFetchRequest,
      @RequestParam Long userId,
      @RequestParam Long languageId) {
    log.debug("Request for fetching List Order-detail for User id: {}", userId);
    OrderFetchRequest orderRequest = orderFetchRequest.toBuilder().userId(userId).languageId(languageId).build();
    OrderReturnListResponse orderReturnListResponse = null;
    try {
      orderReturnListResponse = orderService.getAll(orderRequest);
      if (ObjectUtils.isEmpty(orderReturnListResponse)) {
        log.warn("No Order detail found for requested User-id : {}", userId);
        return new ResponseEntity<>(orderReturnListResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching Order-detail for User id: {}", userId, e);
      return new ResponseEntity<>(orderReturnListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Order List details {} for requested UserId: {}", orderReturnListResponse, userId);
    return new ResponseEntity<>(orderReturnListResponse, HttpStatus.OK);
  }

}
