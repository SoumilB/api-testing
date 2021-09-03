package com.justanalytics.publishapi.dataModels.apiManagement;

import java.util.List;

public class ResponseDao<T> {

    private List<T> value;

    private Integer count;

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
}
