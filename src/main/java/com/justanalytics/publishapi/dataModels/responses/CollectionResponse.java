package com.justanalytics.publishapi.dataModels.responses;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Collection", subTypes = {DataResponse.class})
public class CollectionResponse<T> {
    private List<T> results;

    public CollectionResponse() {}
    
    public CollectionResponse(List<T> results) {
        this.results = results;
    }

	public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
    
}
