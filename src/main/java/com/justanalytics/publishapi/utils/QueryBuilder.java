package com.justanalytics.publishapi.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.justanalytics.publishapi.dataModels.DatabricksDataType;
import com.justanalytics.publishapi.dataModels.DatasetSchema;
import com.justanalytics.publishapi.dataModels.query.Query;
import com.justanalytics.publishapi.dataModels.query.Sort;
import com.justanalytics.publishapi.dataModels.query.filters.BetweenFilter;
import com.justanalytics.publishapi.dataModels.query.filters.ComparisonFilter;
import com.justanalytics.publishapi.dataModels.query.filters.CompoundFilter;
import com.justanalytics.publishapi.dataModels.query.filters.Filter;
import com.justanalytics.publishapi.dataModels.query.filters.INFilter;
import com.justanalytics.publishapi.dataModels.query.filters.SimpleFilter;

import org.springframework.stereotype.Component;

@Component
public class QueryBuilder {
    
    private String buildBetweenFilter(BetweenFilter betweenFilter, Function<String, String> conversionFunction) {
        String fromString = conversionFunction.apply(betweenFilter.getFrom().toString());
        String toString = conversionFunction.apply(betweenFilter.getTo().toString());
        return String.format("%s BETWEEN %s AND %s", betweenFilter.getField(), fromString, toString);
    }

    private String buildComparisonFilter(ComparisonFilter comparisonFilter, Function<String, String> conversionFunction) {
        String valueString = conversionFunction.apply(comparisonFilter.getValue().toString());
        return String.format("%s %s %s", comparisonFilter.getField(), comparisonFilter.getCondition().value, valueString);
    }

    private String buildINFilter(INFilter inFilter, Function<String, String> conversionFunction) {
        String values = inFilter.getValues().stream()
            .map(value -> conversionFunction.apply(value.toString()))
            .collect(Collectors.joining(",", "(", ")"));
        return String.format("%s in %s", inFilter.getField(), values);
    }

    /*
    private Function<String, String> getConversionFunction(String dataType) {
        switch(DatabricksDataType.valueOf(dataType.toUpperCase())) {
            case BYTE:
            case TINYINT:
            case SHORT:
            case SMALLINT:
            case INT:
            case INTEGER:
                return (value) -> ConversionUtil.convertToInteger(value).toString();
            case BIGINT:
            case LONG:
                return (value) -> ConversionUtil.convertToLong(value).toString();
            case BOOLEAN:
                return (value) -> ConversionUtil.convertToBoolean(value).toString();
            case FLOAT:
            case REAL:
                return (value) -> ConversionUtil.convertToFloat(value).toString();
            case DEC:
            case DECIMAL:
            case DOUBLE:
            case NUMERIC:
                return (value) -> ConversionUtil.convertToDouble(value).toString();
            case DATE:
                return (value) -> {
                    String  dateString = ConversionUtil.convertToDate(value).toString();
                    return String.format("'%s'", dateString);
                };
            case TIMESTAMP:
                return (value) -> {
                    String timestampString = ConversionUtil.convertToTimestamp(value).toString();
                    return String.format("'%s'", timestampString);
                };
            case INTERVAL:
            case BINARY:
            default:
                return (value) -> String.format("'%s'", value.toString());
        }
    }*/

    private Function<String, String> getConversionFunction(String dataType) {
        switch(DatabricksDataType.valueOf(dataType.toUpperCase())) {
            case BYTE:
            case TINYINT:
            case SHORT:
            case SMALLINT:
            case INT:
            case INTEGER:
                // return (value) -> ConversionUtil.convertToInteger(value).toString();
            case BIGINT:
            case LONG:
                // return (value) -> ConversionUtil.convertToLong(value).toString();
            case BOOLEAN:
                // return (value) -> ConversionUtil.convertToBoolean(value).toString();
            case FLOAT:
            case REAL:
                // return (value) -> ConversionUtil.convertToFloat(value).toString();
            case DEC:
            case DECIMAL:
            case DOUBLE:
            case NUMERIC:
                // return (value) -> ConversionUtil.convertToDouble(value).toString();
                return value -> value.toString();
            case DATE:
                return (value) -> {
                    String  dateString = ConversionUtil.convertToDate(value).toString();
                    return String.format("'%s'", dateString);
                };
            case TIMESTAMP:
                return (value) -> {
                    String timestampString = ConversionUtil.convertToTimestamp(value).toString();
                    return String.format("'%s'", timestampString);
                };
            case INTERVAL:
            case BINARY:
            default:
                return (value) -> String.format("'%s'", value.toString());
        }
    }

    private String buildSimpleFilter(SimpleFilter simpleFilter, String dataType) {
        Function<String, String> conversionFunction = getConversionFunction(dataType);
        switch(simpleFilter.getType()) {
            case COMPARISON:
                return buildComparisonFilter((ComparisonFilter) simpleFilter, conversionFunction);
            case BETWEEN:
                return buildBetweenFilter((BetweenFilter) simpleFilter, conversionFunction);
            case IN:
                return buildINFilter((INFilter) simpleFilter, conversionFunction);
            default:
                return null;
        }
    }

    private String buildCompoundFilter(CompoundFilter compoundFilter, Map<String, String> schema) {
        String operator = String.format(" %s ", compoundFilter.getOperator().toString());
        return compoundFilter.getFilters().stream()
            .map(filter -> buildFilterQuery(filter, schema))
            .collect(Collectors.joining(operator, "(", ")"));
    }

    private String buildFilterQuery(Filter filter, Map<String, String> schema) {
        String filterQueryString = "";
        switch(filter.getType()) {
            case COMPARISON:
            case BETWEEN:
            case IN:
                SimpleFilter simpleFilter = (SimpleFilter) filter;
                String dataType = schema.get(simpleFilter.getField());
                filterQueryString = buildSimpleFilter(simpleFilter, dataType);
                break;
            case COMPOUND:
                filterQueryString = buildCompoundFilter((CompoundFilter) filter, schema);
                break;
            default:
                break;
        }
        if(filter.getNegate()) {
            return String.format("NOT %s", filterQueryString);
        }
        return filterQueryString;
    }
    
    private String prepareSelectedAttributes(List<String> selectedFields) {
        return selectedFields.stream().collect(Collectors.joining(", "));
    }

    private String buildOrderByString(List<Sort> orderBy) {
        return orderBy.stream().map(sort -> String.format("%s %s", sort.by, sort.order.value))
            .collect(Collectors.joining(", "));
    }

    public String build(Query query, DatasetSchema datasetSchema) {
        StringBuilder queryString = new StringBuilder("SELECT");
        // Selected Attributes
        queryString.append(String.format(" %s", prepareSelectedAttributes(query.select)));
        // FROM System.Dataset
        queryString.append(String.format(" FROM %s.%s", query.getDatabase(), query.getDataset()));
        //Filter
        if(query.filter != null) {
            String filterQuery = buildFilterQuery(query.filter, datasetSchema.getSchema());
            queryString.append(String.format(" WHERE %s", filterQuery));
        }
        //Sort
        if(!query.orderBy.isEmpty()) {
            String sortByString = buildOrderByString(query.orderBy);
            queryString.append(String.format(" ORDER BY %s", sortByString));
        }

        queryString.append(" LIMIT " + query.top);
        
        return queryString.toString();
    }
}
