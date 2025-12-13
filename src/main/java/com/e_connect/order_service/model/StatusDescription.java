package com.e_connect.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "STATUS_DESCRIPTION")
public class StatusDescription extends BaseRecord {
  @EmbeddedId
  private StatusDescriptionEmbeddedId statusDescriptionEmbeddedId;

  @Column(name = "STATUS_DESCRIPTION")
  private String statusDescription;
  @Column(name = "STATUS_NAME")
  private String statusName;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", updatable = false, insertable = false)
  private Status status;

  @Embeddable
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  @Builder(toBuilder = true)
  @Getter
  public static class StatusDescriptionEmbeddedId {
    @Column(name = "LANGUAGE_ID")
    private Long languageId;
    @Column(name = "STATUS_ID")
    private Long statusId;
  }

}
