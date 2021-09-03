package com.justanalytics.publishapi.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import com.justanalytics.publishapi.dataModels.apiManagement.ContractDao;
import com.justanalytics.publishapi.dataModels.apiManagement.ResponseDao;
import com.justanalytics.publishapi.dataModels.apiManagement.SubscriptionContract;
import com.justanalytics.publishapi.dataModels.apiManagement.UserContract;
import com.justanalytics.publishapi.dataModels.telemetry.ApiManagementRequestTelemetry;
import com.justanalytics.publishapi.repositories.ApiManagementRequestTelemetryRepository;
import com.justanalytics.publishapi.repositories.CustomRepository;
import com.justanalytics.publishapi.repositories.SubscriptionContractRepository;
import com.justanalytics.publishapi.repositories.UserContractRepository;
import com.justanalytics.publishapi.utils.APIManagementAccessTokenGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIManagementService {

    private static final Logger logger = LoggerFactory.getLogger(APIManagementService.class);

    @Value("${api-management.service}")
    private String service;

    @Value("${api-management.subscription-id}")
    private String subscriptionId;

    @Value("${api-management.resource-group}")
    private String resourceGroup;

    @Value("${api-management.api-version}")
    private String apiVersion;

    @Autowired
    private APIManagementAccessTokenGenerator tokenGenerator;

    @Autowired
    private ApiManagementRequestTelemetryRepository requestTelemetryRepository;

    @Autowired
    private SubscriptionContractRepository subscriptionRespository;

    @Autowired
    private UserContractRepository userRepository;

    @Autowired
    private CustomRepository customRepository;

    private String generateApiUrl() {
        return String.format(
                "https://%s.management.azure-api.net/subscriptions/%s/resourceGroups/%s/providers/Microsoft.ApiManagement/service/%s",
                service, subscriptionId, resourceGroup, service);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenGenerator.generateAccessToken());
        return headers;
    }

    private HttpEntity<String> getHeadersEntity() {
        return new HttpEntity<>(getHeaders());
    }

    public List<ApiManagementRequestTelemetry> getResportsByRequest(String from) {
        String apiUrl = generateApiUrl();
        String url = String.format("%s/reports/byRequest?api-version=%s&$filter=timestamp ge datetime'%s'", apiUrl, apiVersion, from);
        HttpEntity<String> entity = getHeadersEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDao<ApiManagementRequestTelemetry>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<ResponseDao<ApiManagementRequestTelemetry>>(){}
        );

        return response.getBody().getValue();
    }

    private List<ContractDao> listResourceContractDao(String url) {
        HttpEntity<String> entity = getHeadersEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDao<ContractDao>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<ResponseDao<ContractDao>>() {}
        );
        return response.getBody().getValue();
    }

    private String resolvePropertyValue(Object value) {
        return Optional.ofNullable(value).orElseGet(String::new).toString();
    }

    public List<SubscriptionContract> listSubscriptions() {
        String apiUrl = generateApiUrl();
        String url = String.format("%s/subscriptions?api-version=%s", apiUrl, apiVersion);
        List<ContractDao> contracts = listResourceContractDao(url);
        return contracts.stream().map(contract -> {
            SubscriptionContract subscriptionContract = new SubscriptionContract();
            subscriptionContract.setId(contract.getName());
            subscriptionContract.setName(resolvePropertyValue(contract.getProperties().get("displayName")));
            return subscriptionContract;
        }).collect(Collectors.toList());
    }

    public List<UserContract> listUsers() {
        String apiUrl = generateApiUrl();
        String url = String.format("%s/users?api-version=%s", apiUrl, apiVersion);
        List<ContractDao> contracts = listResourceContractDao(url);
        return contracts.stream().map(contract -> {
            Map<String, Object> properties = contract.getProperties();
            UserContract userContract = new UserContract();
            userContract.setId(contract.getName());
            userContract.setEmail(resolvePropertyValue(properties.getOrDefault("email", "")));
            userContract.setFirstName(resolvePropertyValue(properties.getOrDefault("firstName", "")));
            userContract.setLastName(resolvePropertyValue(properties.getOrDefault("lastName", "")));
            return userContract;
        }).collect(Collectors.toList());
    }

    public List<ApiManagementRequestTelemetry> saveRequestTelemetries(List<ApiManagementRequestTelemetry> requestTelemetries) {
        List<ApiManagementRequestTelemetry> savedEntities = new ArrayList<>();
        requestTelemetries.forEach(requestTelemetry -> {
            try {
                customRepository.insertRequestTelemetry(requestTelemetry);
                savedEntities.add(requestTelemetry);
            } catch (EntityExistsException | DataIntegrityViolationException e) {
                logger.warn(String.format("Entity with id: %s already exists", requestTelemetry.getRequestId()));
            }
        });
        return savedEntities;
    }

    public void deleteByTimestampAfter(Timestamp timestamp) {
        requestTelemetryRepository.deleteByTimestampAfter(timestamp);
    }

    public List<SubscriptionContract> saveSubscriptions(List<SubscriptionContract> subscriptions) {
        return (List<SubscriptionContract>) subscriptionRespository.saveAll(subscriptions);
    }

    public List<UserContract> saveUsers(List<UserContract> users) {
        return (List<UserContract>) userRepository.saveAll(users);
    }

    public Timestamp getMaxTimestamp() {
        return requestTelemetryRepository.getMaxTimestamp();
    }
    
}
