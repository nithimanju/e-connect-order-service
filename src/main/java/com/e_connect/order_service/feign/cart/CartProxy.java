package com.e_connect.order_service.feign.cart;

import java.util.List;

import org.springframework.stereotype.Service;

import com.e_connect.order_service.dto.CartDetailResponse;
import com.e_connect.order_service.dto.CartRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartProxy {
  private final CartClient cartClient;

  public CartDetailResponse get(Long cartId, Long userId, List<Long> statusIds) {
    try {
      return cartClient.get(cartId, userId, statusIds).getBody();
    } catch (Exception e) {
      log.error("Error fetching Cart Response", e);
      return null;
    }
  }

  public Long put(CartRequest cartRequest) {
    try {
      return cartClient.put(cartRequest).getBody();
    } catch (Exception e) {
      log.error("Error fetching Cart Response", e);
      return null;
    }
  }
}
