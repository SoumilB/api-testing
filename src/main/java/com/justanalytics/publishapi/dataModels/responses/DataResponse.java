package com.justanalytics.publishapi.dataModels.responses;

import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "DataResponse")
public class DataResponse extends CollectionResponse<Map<String, Object>> {
    private Long count;

    public DataResponse() {}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
