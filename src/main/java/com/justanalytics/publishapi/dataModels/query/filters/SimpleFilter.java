package com.justanalytics.publishapi.dataModels.query.filters;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Filter.class)
public abstract class SimpleFilter extends Filter {
    @ApiModelProperty(hidden = true)
    private FilterType type;
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public abstract List<Object> getValues();
    
}
