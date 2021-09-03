package com.justanalytics.publishapi.repositories;

import java.util.List;

import com.justanalytics.publishapi.dataModels.authorization.SystemRegistration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRegistrationRepository extends CrudRepository<SystemRegistration, Integer> {
    
    public Boolean existsByProductNameAndSystemName(String productId, String systemName);

    public List<SystemRegistration> findByProductName(String productId);
}
