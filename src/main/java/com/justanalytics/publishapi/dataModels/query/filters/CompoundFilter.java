package com.justanalytics.publishapi.dataModels.query.filters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Filter.class)
public class CompoundFilter extends Filter {

    @ApiModelProperty(hidden = true)
    private FilterType type;
    private List<Filter> filters;
    private LogicalOperator operator;

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public void setOperator(LogicalOperator operator) {
        this.operator = operator;
    }

    @JsonIgnore
    Map<String, List<Object>> getAllFieldValueMap() {
        return filters.stream().flatMap(filter -> filter.getFieldValueMap().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }    
}
