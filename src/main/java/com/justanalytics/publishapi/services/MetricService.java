package com.justanalytics.publishapi.services;

import com.justanalytics.publishapi.dataModels.telemetry.RequestTelemetry;
import com.justanalytics.publishapi.repositories.CustomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricService {
    
    @Autowired
    private CustomRepository customRepository;

    public void saveRequestTelemetry(RequestTelemetry requestTelemetry) {
        customRepository.insertRequestTelemetry(requestTelemetry);
    }

}
