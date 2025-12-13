package com.e_connect.order_service.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.e_connect.order_service.model.PaymentDescritpion;

@Repository
public interface PaymentTypeDescriptionRepository extends JpaRepository<PaymentDescritpion, Long>, JpaSpecificationExecutor<PaymentDescritpion>{
    
}
