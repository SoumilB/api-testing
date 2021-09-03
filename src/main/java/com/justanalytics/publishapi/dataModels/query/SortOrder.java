package com.justanalytics.publishapi.dataModels.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortOrder {
    @JsonProperty(value = "ascending")
    Ascending("ASC"),
    @JsonProperty(value = "descending")
    Descending("DESC");

    public String value;

    private SortOrder(String value) {
        this.value = value;
    }
}
