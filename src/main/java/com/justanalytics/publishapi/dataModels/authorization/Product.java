package com.justanalytics.publishapi.dataModels.authorization;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String name;

    private String displayName;

    private Integer rowsLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getRowsLimit() {
        return rowsLimit;
    }

    public void setRowsLimit(Integer rowsLimit) {
        this.rowsLimit = rowsLimit;
    }
    
}
