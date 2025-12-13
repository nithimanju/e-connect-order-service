package com.e_connect.order_service.status.dto;

import java.io.Serializable;

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
public class Status implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long statusId;
  private String statusDescription;
  private String statusName;
  private Long languageId;
}