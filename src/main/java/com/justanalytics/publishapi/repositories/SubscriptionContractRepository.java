package com.justanalytics.publishapi.repositories;

import com.justanalytics.publishapi.dataModels.apiManagement.SubscriptionContract;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionContractRepository extends CrudRepository<SubscriptionContract, String> {
    
}
