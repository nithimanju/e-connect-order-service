package com.e_connect.order_service.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

import com.e_connect.order_service.Constants;
import com.e_connect.order_service.model.PaymentDescritpion;
import com.e_connect.order_service.payment.dto.PaymentType;
import com.e_connect.order_service.payment.repository.PaymentTypeDescriptionRepository;
import com.e_connect.order_service.utils.PredicateUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {

  private final PaymentTypeDescriptionRepository paymentTypeRepository;
    private final PredicateUtilsService<PaymentDescritpion> predicateUtilsService;

  @Cacheable(value = Constants.PAYMENT_TYPE_DESCRIPTION_CACHE, key="#paymentTypeId+':'+#languageId")
  public PaymentType get(Long paymentTypeId, Long languageId) {
    PaymentType resPaymentType = null;
    Specification<PaymentDescritpion> paymentSpecs = paymentSpecification(languageId,
        paymentTypeId);
    List<PaymentDescritpion> paymentType = paymentTypeRepository.findAll(paymentSpecs);
    if (ObjectUtils.isNotEmpty(paymentType)) {
      resPaymentType = buildResponse(paymentType.get(0), languageId);
    }
    return resPaymentType;
  }

  private Specification<PaymentDescritpion> paymentSpecification(Long languageId,
      Long paymentTypeId) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates = predicateUtilsService.andValidePredicate(predicates, root.get("paymentTypeDescriptionEmbeddedId"), "languageId", languageId);
      predicates = predicateUtilsService.andValidePredicate(predicates, root.get("paymentTypeDescriptionEmbeddedId"), "paymentTypeId", paymentTypeId);
      predicates = predicateUtilsService.andValidePredicate(predicates, root.join("paymentType"), "isActive", true);

      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }
  @Cacheable(value = Constants.PAYMENT_TYPE_DESCRIPTION_LIST_CACHE, key="#languageId")
  public Map<Long, PaymentType> getAll(Long languageId) {
    Map<Long, PaymentType> resPaymentTypes = new HashMap<>();
    Specification<PaymentDescritpion> paymentSpecs = paymentSpecification(languageId, null);
    List<PaymentDescritpion> paymentTypes = paymentTypeRepository.findAll(paymentSpecs);
    if (ObjectUtils.isNotEmpty(paymentTypes)) {
      resPaymentTypes = paymentTypes.stream().map(paymentType -> buildResponse(paymentType, languageId))
          .collect(Collectors.toMap(
              PaymentType::getPaymentTypeId, paymentType -> paymentType, (oldValue, newValue) -> newValue,
              HashMap::new));
    }
    return resPaymentTypes;
  }

  private PaymentType buildResponse(PaymentDescritpion paymentType, Long LanguageId) {
    return PaymentType.builder()
        .paymentTypeId(paymentType.getPaymentType().getPaymentTypeId())
        .paymentTypeDescription(paymentType.getPaymentTypeDescription())
        .paymentTypeName(paymentType.getPaymentTypeName())
        .languageId(LanguageId)
        .build();
  }
}
