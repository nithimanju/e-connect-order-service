package com.e_connect.order_service.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.e_connect.order_service.model.StatusDescription;
import com.e_connect.order_service.model.StatusDescription.StatusDescriptionEmbeddedId;

@Repository
public interface StatusDescriptionRepository extends JpaRepository<StatusDescription, StatusDescriptionEmbeddedId>, JpaSpecificationExecutor<StatusDescription> {
    
}
