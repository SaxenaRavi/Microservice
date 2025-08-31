package com.product.catalog.service.product_catalog_service.service;

import com.product.catalog.service.product_catalog_service.kafka.ProductEventPublisher;
import com.product.catalog.service.product_catalog_service.model.Product;
import com.product.catalog.service.product_catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @Mock
    ProductEventPublisher publisher;

    @InjectMocks
    ProductService service;

    @Test
    void createProduct_savesAndPublishes() {
        Product p = new Product("p-1", "Name", "Desc", new BigDecimal("10.00"), "cat", 10);
        when(repository.save(any(Product.class))).thenReturn(p);

        Product created = service.createProduct(p);

        assertNotNull(created);
        assertEquals("p-1", created.getProductId());
        verify(repository, times(1)).save(p);
        verify(publisher, times(1)).publishProductCreated(p);
    }

    @Test
    void updateProduct_existing_updatesAndPublishes() {
        Product existing = new Product("p-2", "Old", "Old Desc", new BigDecimal("5.00"), "old", 3);
        Product updated = new Product("p-2", "New", "New Desc", new BigDecimal("7.50"), "new", 6);

        when(repository.findById("p-2")).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultOpt = service.updateProduct("p-2", updated);

        assertTrue(resultOpt.isPresent());
        Product result = resultOpt.get();
        assertEquals("New", result.getName());
        assertEquals(6, result.getQuantityAvailable());
        verify(publisher, times(1)).publishProductUpdated(result);
    }

    @Test
    void deleteProduct_existing_deletesAndPublishes() {
        when(repository.existsById("p-3")).thenReturn(true);

        boolean deleted = service.deleteProduct("p-3");

        assertTrue(deleted);
        verify(repository, times(1)).deleteById("p-3");
        verify(publisher, times(1)).publishProductDeleted("p-3");
    }
}
