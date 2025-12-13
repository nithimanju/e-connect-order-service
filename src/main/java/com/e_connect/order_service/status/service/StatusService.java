package com.e_connect.order_service.status.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.e_connect.order_service.Constants;
import com.e_connect.order_service.model.StatusDescription;
import com.e_connect.order_service.status.dto.Status;
import com.e_connect.order_service.status.repository.StatusDescriptionRepository;
import com.e_connect.order_service.utils.PredicateUtilsService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class StatusService {
  private final StatusDescriptionRepository statusDescriptionRepository;
  private final PredicateUtilsService<StatusDescription> predicateUtilsService;

  @Cacheable(value = Constants.STATUS_NAME_CACHE)
  public Status get(Long statusId, Long languageId) {
    Status resStatus = null;
    Specification<StatusDescription> statusSpecs = statusSpecification(languageId,
        statusId);
    List<StatusDescription> status = statusDescriptionRepository.findAll(statusSpecs);
    if (ObjectUtils.isNotEmpty(status)) {
      resStatus = buildResponse(status.get(0), languageId);
    }
    return resStatus;
  }

  private Specification<StatusDescription> statusSpecification(Long languageId,
      Long statusId) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates = predicateUtilsService.andValidePredicate(predicates, root.get("statusDescriptionEmbeddedId"),
          "languageId", languageId);
      predicates = predicateUtilsService.andValidePredicate(predicates, root.get("statusDescriptionEmbeddedId"),
          "statusId", statusId);
      predicates = predicateUtilsService.andValidePredicate(predicates, root.get("status"), "isActive", true);

      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  @Cacheable(value = Constants.STATUS_LIST_CACHE, key = "#languageId")
  public Map<Long, Status> getAll(Long languageId) {
    Map<Long, Status> resStatuses = new HashMap<>();
    Specification<StatusDescription> statusSpecs = statusSpecification(languageId, null);
    List<StatusDescription> statuses = statusDescriptionRepository.findAll(statusSpecs);
    if (ObjectUtils.isNotEmpty(statuses)) {
      resStatuses = statuses.stream().map(Status -> buildResponse(Status, languageId))
          .collect(Collectors.toMap(
              Status::getStatusId, status -> status, (oldValue, newValue) -> newValue,
              HashMap::new));
    }
    return resStatuses;
  }

  private Status buildResponse(StatusDescription status, Long LanguageId) {
    return Status.builder()
        .statusId(status.getStatus().getStatusId())
        .statusDescription(status.getStatusDescription())
        .statusName(status.getStatusName())
        .languageId(LanguageId)
        .build();
  }
}
