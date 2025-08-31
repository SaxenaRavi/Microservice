package com.product.catalog.service.product_catalog_service.repository;

import com.product.catalog.service.product_catalog_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
