package com.justanalytics.publishapi.dataModels;

import java.io.Serializable;
import java.util.Map;


public class DatasetSchema implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String database;
    private String dataset;
    private Map<String, String> schema;

    public DatasetSchema(String database, String dataset, Map<String, String> schema) {
        this.database = database;
        this.dataset = dataset;
        this.schema = schema;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Map<String, String> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, String> schema) {
        this.schema = schema;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
