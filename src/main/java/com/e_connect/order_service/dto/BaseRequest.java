package com.e_connect.order_service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BaseRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long userId;
  private Boolean isGuest;
  private Long languageId;
}
