package com.justanalytics.publishapi.rest;

import java.sql.SQLException;

import com.justanalytics.publishapi.dataModels.responses.ErrorResponse;
import com.justanalytics.publishapi.dataModels.responses.CollectionResponse;
import com.justanalytics.publishapi.dataModels.responses.DataResponse;
import com.justanalytics.publishapi.exceptions.UnAccessibleSystemException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.justanalytics.publishapi.dataModels.DatasetSchema;
import com.justanalytics.publishapi.dataModels.query.Query;
import com.justanalytics.publishapi.services.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    private static final String PRODUCT_ID_HEADER = "x-request-product-id";

    @Autowired
    private DataService dataService;

    @ApiOperation(value = "Get Databases", nickname = "getDatabases")
    @GetMapping("/databases")
    public ResponseEntity<CollectionResponse<String>> getDatabases(
        @ApiParam(hidden = true)
        @RequestHeader(PRODUCT_ID_HEADER) String productId
    ) {
        return ResponseEntity.ok(dataService.getSystemsByProductId(productId));
    }

    @ApiOperation(value = "Get Datasets with Schema", nickname = "getDatasetsWithSchema")
    @GetMapping("/databases/{database}/datasets")
    public ResponseEntity<CollectionResponse<DatasetSchema>> getDatasetSchema(
        @ApiParam(hidden = true)
        @RequestHeader(PRODUCT_ID_HEADER) String productId,
        @PathVariable("database") String database
    ) {
        if(dataService.checkAccess(productId, database))            
            return ResponseEntity.ok(dataService.getDatasetsWithSchemas(database));
        
        throw new UnAccessibleSystemException();
    }

    @ApiOperation(value = "Get Dataset Schema", nickname = "getDatasetSchema")
    @GetMapping("/databases/{database}/datasets/{dataset}")
    public ResponseEntity<DatasetSchema> getDatasetSchema(
        @PathVariable("database") String database,
        @PathVariable("dataset") String dataset,
        @ApiParam(hidden = true)
        @RequestHeader(PRODUCT_ID_HEADER) String productId
    ) {
        if(dataService.checkAccess(productId, database))
            return ResponseEntity.ok(dataService.getDatasetSchema(database, dataset));
        throw new UnAccessibleSystemException();
    }

    @ApiOperation(value = "Basic Query", nickname = "queryBasic")
    @GetMapping("/queryBasic")
    public ResponseEntity<DataResponse> queryBasic(
        @ApiParam(hidden = true)
        @RequestHeader(PRODUCT_ID_HEADER) String productId,
        @RequestParam(name = "database") String database,
        @RequestParam(name = "dataset") String dataset,
        @ApiParam(
            value = "Expressiong to put in WHERE clause of query",
            example = "timestamp>='2020-10-31' and Month=2 and (Year=2000 or FlightNum in (2866, 2366, 3749, 1959, 6336))"
        )
        @RequestParam(name = "filter", required = false) String filter,
        @ApiParam(
            value = "List of comma sepearted expressions to sort the results",
            example = "business_dt, FlightNum desc"
        )
        @RequestParam(name = "orderBy", required = false) String orderBy,
        @RequestParam(name = "count", required = false, defaultValue = "false") Boolean count,
        @ApiParam(
            value = "Select columns from the table. By default it is *",
            example = "business_dt, Year, Month, ArrTime, DepTime"
        )
        @RequestParam(name = "select", required = false, defaultValue = "*") String select,
        @RequestParam(name = "top", required = false, defaultValue = "50") Integer top
    ) {
        if(dataService.checkAccess(productId, database)) {
            String query = dataService.prepareQuery(database, dataset, filter, orderBy, select, top);
            DataResponse dataResponse = dataService.handleBasicQuery(database, dataset, query, count);
            return ResponseEntity.ok()
                .header("row-count", "" + dataResponse.getResults().size())
                .body(dataResponse);
        }
        throw new UnAccessibleSystemException();
    }

    @ApiOperation(value = "Query Data", nickname = "queryData")
    @PostMapping("/query")
    public ResponseEntity<DataResponse> queryData(
        @ApiParam(hidden = true)
        @RequestHeader(PRODUCT_ID_HEADER) String productId,
        @RequestBody Query query
    ) throws SQLException {
        if(dataService.checkAccess(productId, query.getDatabase())) {
            DataResponse dataResponse = dataService.queryData(productId, query);
            return ResponseEntity.ok()
                .header("row-count", "" + dataResponse.getResults().size())
                .body(dataResponse);
        }
        throw new UnAccessibleSystemException();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleBaseException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({UnrecognizedPropertyException.class})
    public ResponseEntity<ErrorResponse> handleException(UnrecognizedPropertyException e) {
        ErrorResponse response = new ErrorResponse(String.format("Unknown property: %s", e.getPropertyName()));
        return ResponseEntity.badRequest().body(response);
    }
}