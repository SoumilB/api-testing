package com.justanalytics.publishapi.utils;

import java.util.Set;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.justanalytics.publishapi.dataModels.DatabricksDataType;
import com.justanalytics.publishapi.dataModels.DatasetSchema;
import com.justanalytics.publishapi.dataModels.query.Query;
import com.justanalytics.publishapi.dataModels.query.filters.Filter;

import org.springframework.stereotype.Component;

@Component
public class QueryValidator {

    private Boolean validateDataType(String dataType, List<Object> values) throws ClassCastException {
        try {
            switch(DatabricksDataType.valueOf(dataType.toUpperCase())) {
                case BYTE:
                case TINYINT:
                case SHORT:
                case SMALLINT:
                case INT:
                case INTEGER:
                    ConversionUtil.<Integer>convertTo(ConversionUtil::convertToInteger, values);
                    return true;
                case BIGINT:
                case LONG:
                    ConversionUtil.<Long>convertTo(ConversionUtil::convertToLong, values);
                    return true;
                case BOOLEAN:
                    ConversionUtil.<Boolean>convertTo(ConversionUtil::convertToBoolean, values);
                    return true;
                case FLOAT:
                case REAL:
                    ConversionUtil.<Float>convertTo(ConversionUtil::convertToFloat, values);
                    return true;
                case DEC:
                case DECIMAL:
                case DOUBLE:
                case NUMERIC:
                    ConversionUtil.<Double>convertTo(ConversionUtil::convertToDouble, values);
                    return true;
                case DATE:
                    ConversionUtil.<Date>convertTo(ConversionUtil::convertToDate, values);
                    return true;
                case TIMESTAMP:
                    ConversionUtil.<Timestamp>convertTo(ConversionUtil::convertToTimestamp, values);
                    return true;
                case INTERVAL:
                case BINARY:
                    return true;
                default:
                    return true;
            }
        } catch(NumberFormatException | DateTimeParseException e) {
            return false;
        }
    }

    private Boolean validateFilter(Filter filter, Map<String, String> schema) throws SQLException {
        Map<String, List<Object>> filterFieldValueMap = filter.getFieldValueMap();
        Map<String, List<Object>> unvalidatedFieldValueMap = filterFieldValueMap.entrySet().stream()
            .filter(entry -> {
                String dataType = schema.get(entry.getKey());
                return !validateDataType(dataType, entry.getValue());
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if(!unvalidatedFieldValueMap.isEmpty()) {
            String unvalidatedFields = unvalidatedFieldValueMap.keySet().stream().collect(Collectors.joining(", "));
            throw new SQLException(String.format("DataType mismatch for: %s", unvalidatedFields));
        }
        return true;
    }

    private Boolean validateFields(Set<String> fields, Set<String> schemaFields) throws SQLException {
        fields.removeAll(schemaFields);
        if(fields.size() > 0) {
            throw new SQLException(String.format("These field/s are not present in dataset: %s", fields.stream().collect(Collectors.joining(", "))));
        }
        return true;
    }

    public Boolean validate(Query query, DatasetSchema datasetSchema) throws SQLException {
        validateFields(query.getAllFields(), datasetSchema.getSchema().keySet());
        if(query.filter != null)
            validateFilter(query.filter, datasetSchema.getSchema());
        return true;
    }
    
}
