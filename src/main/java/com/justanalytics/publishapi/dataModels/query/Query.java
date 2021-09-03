package com.justanalytics.publishapi.dataModels.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justanalytics.publishapi.dataModels.query.filters.Filter;

public class Query {
    @NotNull
    private String database;
    @NotNull
    private String dataset;
    public Filter filter;
    public List<Sort> orderBy = new ArrayList<>();
    public List<String> select = Collections.singletonList("*");
    public Integer top = 50;
    public Boolean count = false;

    @JsonIgnore
    public Set<String> getAllFields() {
        Set<String> fields = new HashSet<>();
        if(this.filter != null) {
            fields.addAll(this.filter.getFieldValueMap().keySet());
        }
        fields.addAll(this.orderBy.stream().map(sort -> sort.by).collect(Collectors.toList()));
        if(!(this.select.size() == 1 && this.select.get(0).equals("*"))) {
            fields.addAll(this.select);
        }
        return fields;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
