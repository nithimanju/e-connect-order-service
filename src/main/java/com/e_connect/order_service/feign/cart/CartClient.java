package com.e_connect.order_service.feign.cart;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.e_connect.order_service.dto.CartDetailResponse;
import com.e_connect.order_service.dto.CartRequest;

@FeignClient(value = "cart-service")
public interface CartClient {
  @GetMapping(value = "/cart")
  public ResponseEntity<CartDetailResponse> get(@RequestParam Long cartId, @RequestParam Long userId);
  @PutMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> put(@RequestBody CartRequest cartRequest);
}
