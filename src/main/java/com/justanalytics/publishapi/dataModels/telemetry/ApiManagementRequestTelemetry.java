package com.justanalytics.publishapi.dataModels.telemetry;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApiManagementRequestTelemetry {

    @Id
    private String requestId;
    private String apiId;
    private String apiRegion;
    private double apiTime;
    private String backendResposeCode;
    private String cache;
    private String ipAddress;
    private String method;
    private String operationId;
    private String productId;
    private Integer requestSize;
    private Integer responseCode;
    private Integer responseSize;
    private Double serviceTime;
    private String subscriptionId;
    private Timestamp timestamp;
    private String url;
    private String userId;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiRegion() {
        return apiRegion;
    }

    public void setApiRegion(String apiRegion) {
        this.apiRegion = apiRegion;
    }

    public double getApiTime() {
        return apiTime;
    }

    public void setApiTime(double apiTime) {
        this.apiTime = apiTime;
    }

    public String getBackendResposeCode() {
        return backendResposeCode;
    }

    public void setBackendResposeCode(String backendResposeCode) {
        this.backendResposeCode = backendResposeCode;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(Integer requestSize) {
        this.requestSize = requestSize;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(Integer responseSize) {
        this.responseSize = responseSize;
    }

    public Double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }   
}