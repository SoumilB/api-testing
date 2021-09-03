package com.justanalytics.publishapi.repositories;

import com.justanalytics.publishapi.dataModels.apiManagement.UserContract;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContractRepository extends CrudRepository<UserContract, String> {
    
}
