package com.e_connect.order_service.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "STATUS")
public class Status extends BaseRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "STATUS_ID")
  private Long statusId;
  @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
  private List<StatusDescription> statusDescriptionId;
  @Column(name = "STATUS_TYPE_ID")
  private Long statusTypeId;
  @Column(name = "ACTIVE")
  private Boolean isActive;
}
