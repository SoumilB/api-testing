package com.justanalytics.publishapi.dataModels.telemetry;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RequestTelemetry {

    @Id
    private String requestId;
    private String path;
    private String method;
    private Integer responseStatusCode;
    private Long executionTime;
    private Integer rowCount;
    private Timestamp timestamp;

    public RequestTelemetry(String requestId, String path, String method, Integer responseStatusCode, Long executionTime,
            Timestamp timestamp) {
        this.requestId = requestId;
        this.path = path;
        this.method = method;
        this.responseStatusCode = responseStatusCode;
        this.executionTime = executionTime;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
