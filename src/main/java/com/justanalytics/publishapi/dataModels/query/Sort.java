package com.justanalytics.publishapi.dataModels.query;

public class Sort {
    public String by;
    public SortOrder order;

    public Sort() {}

    public Sort(String by) {
        this.by = by;
        this.order = SortOrder.Ascending;
    }

    public Sort(String by, SortOrder order) {
        this.by = by;
        this.order = order;
    }
}
