package com.justanalytics.publishapi.services;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.justanalytics.publishapi.dataModels.responses.CollectionResponse;
import com.justanalytics.publishapi.dataModels.responses.DataResponse;
import com.justanalytics.publishapi.dataModels.DatasetSchema;
import com.justanalytics.publishapi.dataModels.authorization.SystemRegistration;
import com.justanalytics.publishapi.dataModels.query.Query;
import com.justanalytics.publishapi.repositories.DataRepository;
import com.justanalytics.publishapi.repositories.ProductRepository;
import com.justanalytics.publishapi.repositories.SystemRegistrationRepository;
import com.justanalytics.publishapi.utils.QueryBuilder;
import com.justanalytics.publishapi.utils.QueryValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private QueryValidator queryValidator;

    @Autowired
    private SystemRegistrationRepository systemRegistrationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QueryBuilder queryBuilder;

    public DatasetSchema getDatasetSchema(String system, String dataset) {
        return dataRepository.getDatasetSchema(system, dataset);
    }

    public CollectionResponse<String> getDatasets(String system) {
        return new CollectionResponse<>(dataRepository.getDatasets(system));
    }

    public CollectionResponse<DatasetSchema> getDatasetsWithSchemas(String system) {
        List<String> tables = dataRepository.getDatasets(system);
        List<DatasetSchema> datasetSchemas = tables.stream().map(table -> dataRepository.getDatasetSchema(system, table))
                .collect(Collectors.toList());
        return new CollectionResponse<>(datasetSchemas);
    }

    public CollectionResponse<String> getSystemsByProductId(String productId) {
        List<SystemRegistration> systems = systemRegistrationRepository.findByProductName(productId);
        List<String> systemNames = systems.stream()
            .map(system -> system.getSystem().getName())
            .collect(Collectors.toList());
        return new CollectionResponse<>(systemNames);
    }

    public CollectionResponse<String> getSystems() {
        return new CollectionResponse<>(dataRepository.getDatabases());
    }

    public Boolean checkAccess(String productId, String systemName) {
        return systemRegistrationRepository.existsByProductNameAndSystemName(productId, systemName);
    }

    private String resolveOrderByString(String orderBy) {
        String[] orderBySplit = orderBy.trim().split(" ");
        if(orderBySplit.length == 1) {
            return String.format("%s %s", orderBySplit[0], "ASC");
        }
        return String.format("%s %s", orderBySplit[0], orderBySplit[1]);
    }

    public String prepareQuery(
        String system,
        String dataset,
        String filter,
        String orderBy,
        String select,
        Integer top
    ) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(String.format("SELECT %s FROM %s.%s", select, system, dataset));

        if(filter != null && !filter.isBlank()) {
            queryBuilder.append(String.format(" WHERE %s", filter));
        }

        if(orderBy != null && !orderBy.isBlank()) {
            String orderByQuery = Arrays.stream(orderBy.split(","))
                .map(o -> resolveOrderByString(o))
                .collect(Collectors.joining(", "));
            queryBuilder.append(String.format(" ORDER BY %s", orderByQuery));
        }

        queryBuilder.append(" LIMIT " + top);

        return queryBuilder.toString();
    }

    public DataResponse handleBasicQuery(
        String system,
        String dataset,
        String query, 
        Boolean count
    ) {
        List<Map<String, Object>> results = dataRepository.getData(query);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setResults(results);
        if(count) {
            dataResponse.setCount(dataRepository.getCount(system, dataset));
        }
        return dataResponse;
    }

    public DataResponse queryData(String productId, Query query) throws SQLException {
        Integer rowsLimit = productRepository.findById(productId).get().getRowsLimit();
        query.top = Math.min(query.top, rowsLimit);
        DatasetSchema datasetSchema = this.getDatasetSchema(query.getDatabase(), query.getDataset());
        queryValidator.validate(query, datasetSchema);
        String queryString = queryBuilder.build(query, datasetSchema);
        List<Map<String, Object>> results = dataRepository.getData(queryString);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setResults(results);
        if(query.count)
            dataResponse.setCount(dataRepository.getCount(query.getDatabase(), query.getDataset()));
        return dataResponse;
    }
}
