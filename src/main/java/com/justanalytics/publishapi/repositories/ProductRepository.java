package com.justanalytics.publishapi.repositories;

import com.justanalytics.publishapi.dataModels.authorization.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    
}
