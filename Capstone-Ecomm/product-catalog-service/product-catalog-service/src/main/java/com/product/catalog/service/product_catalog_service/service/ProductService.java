package com.product.catalog.service.product_catalog_service.service;

import com.product.catalog.service.product_catalog_service.kafka.ProductEventPublisher;
import com.product.catalog.service.product_catalog_service.model.Product;
import com.product.catalog.service.product_catalog_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductEventPublisher publisher;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository repository, ProductEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public Product createProduct(Product product) {
        try {
            log.info("Creating product id={}", product.getProductId());
            Product saved = repository.save(product);
            publisher.publishProductCreated(saved);
            log.info("Product created id={}", saved.getProductId());
            return saved;
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw e;
        }
    }

    public Optional<Product> getProduct(String id) {
        return repository.findById(id);
    }

    public List<Product> listProducts() {
        return repository.findAll();
    }

    public Optional<Product> updateProduct(String id, Product updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setCategory(updated.getCategory());
            existing.setQuantityAvailable(updated.getQuantityAvailable());
            Product saved = repository.save(existing);
            publisher.publishProductUpdated(saved);
            return saved;
        });
    }

    public boolean deleteProduct(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            publisher.publishProductDeleted(id);
            return true;
        }
        return false;
    }
}
