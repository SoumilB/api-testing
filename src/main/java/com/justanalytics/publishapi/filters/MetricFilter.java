package com.justanalytics.publishapi.filters;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.justanalytics.publishapi.dataModels.telemetry.RequestTelemetry;
import com.justanalytics.publishapi.services.MetricService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(2)
public class MetricFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "x-request-id";

    @Autowired
    private MetricService metricService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            String requestId = request.getHeader(REQUEST_ID_HEADER);

            String path = request.getServletPath();
            String method = request.getMethod();
            Long startTime = System.currentTimeMillis();

            filterChain.doFilter(request, response);

            Long endTime = System.currentTimeMillis();
            Long executionTime = endTime - startTime;
            Integer responseStatus = response.getStatus();

            if(requestId != null && !requestId.isBlank()) {
                
                RequestTelemetry requestTelemetry = new RequestTelemetry(
                    requestId, 
                    path,
                    method,
                    responseStatus, 
                    executionTime,
                    Timestamp.from(Instant.now())
                );

                if(responseStatus.equals(200) && 
                    ((path.equals("/api/query") && method.equals("POST")) ||
                        (path.equals("/api/queryBasic") && method.equals("GET")))
                ) {
                    String rowCount = response.getHeader("row-count");
                    if(rowCount != null)
                        requestTelemetry.setRowCount(Integer.parseInt(rowCount));
                }

                metricService.saveRequestTelemetry(requestTelemetry);
            }
    }
    
}
