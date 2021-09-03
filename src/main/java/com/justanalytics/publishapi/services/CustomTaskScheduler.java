package com.justanalytics.publishapi.services;

import java.sql.Timestamp;
import java.util.List;

import com.justanalytics.publishapi.dataModels.apiManagement.SubscriptionContract;
import com.justanalytics.publishapi.dataModels.apiManagement.UserContract;
import com.justanalytics.publishapi.dataModels.telemetry.ApiManagementRequestTelemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomTaskScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomTaskScheduler.class);

    @Value("${telemetry_update_span}")
    private Integer telemetryUpdateSpan;

    @Autowired
    private APIManagementService apiManagementService;
    
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional(propagation = Propagation.REQUIRED)
    public void syncApiManagementAnalytics() {
        Timestamp from = apiManagementService.getMaxTimestamp();
        List<ApiManagementRequestTelemetry> requestTelemetries = apiManagementService.getResportsByRequest(from.toInstant().toString());
        try {
            requestTelemetries = apiManagementService.saveRequestTelemetries(requestTelemetries);
            logger.info(requestTelemetries.size() + " records saved");
        } catch(Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional(propagation = Propagation.REQUIRED)
    public void syncApiManagementMasterData() {
        List<SubscriptionContract> subscriptions = apiManagementService.listSubscriptions();
        apiManagementService.saveSubscriptions(subscriptions);
        List<UserContract> users = apiManagementService.listUsers();
        apiManagementService.saveUsers(users);
    }
}
