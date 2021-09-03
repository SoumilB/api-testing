package com.justanalytics.publishapi.repositories;

import java.sql.Timestamp;

import com.justanalytics.publishapi.dataModels.telemetry.ApiManagementRequestTelemetry;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApiManagementRequestTelemetryRepository extends CrudRepository<ApiManagementRequestTelemetry, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ApiManagementRequestTelemetry amrt WHERE amrt.timestamp >= :timestamp")
    public void deleteByTimestampAfter(@Param("timestamp") Timestamp timestamp);


    @Query("SELECT max(timestamp) FROM ApiManagementRequestTelemetry")
    public Timestamp getMaxTimestamp();

}
