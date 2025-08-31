package com.product.catalog.service.product_catalog_service.controller;

import com.product.catalog.service.product_catalog_service.model.Product;
import com.product.catalog.service.product_catalog_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = service.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        return service.getProduct(productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        return service.updateProduct(productId, product)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        boolean deleted = service.deleteProduct(productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Product> listProducts() {
        return service.listProducts();
    }

    // Batch endpoint to add multiple products in one request
    @PostMapping("/batch")
    public ResponseEntity<List<Product>> addBatch(@RequestBody List<Product> products) {
        List<Product> saved = products.stream().map(service::createProduct).toList();
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Inventory retrieval - here we return quantity_available; an integration point with inventory service could be added
    @GetMapping("/inventory/{productId}")
    public ResponseEntity<Integer> getInventory(@PathVariable String productId) {
        return service.getProduct(productId)
                .map(p -> ResponseEntity.ok(p.getQuantityAvailable()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
