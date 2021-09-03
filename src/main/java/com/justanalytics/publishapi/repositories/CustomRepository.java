package com.justanalytics.publishapi.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.justanalytics.publishapi.dataModels.telemetry.ApiManagementRequestTelemetry;
import com.justanalytics.publishapi.dataModels.telemetry.RequestTelemetry;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertRequestTelemetry(ApiManagementRequestTelemetry requestTelemetry) {
        this.entityManager.persist(requestTelemetry);
    }

    @Transactional
    public void insertRequestTelemetry(RequestTelemetry requestTelemetry) {
        this.entityManager.persist(requestTelemetry);
    }
    
}
