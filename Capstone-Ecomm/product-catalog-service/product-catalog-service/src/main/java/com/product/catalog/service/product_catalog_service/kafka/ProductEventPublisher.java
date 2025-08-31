package com.product.catalog.service.product_catalog_service.kafka;

import com.product.catalog.service.product_catalog_service.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ProductEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductCreated(Product product) {
        publishEvent("product-created", product);
    }

    public void publishProductUpdated(Product product) {
        publishEvent("product-updated", product);
    }

    public void publishProductDeleted(String productId) {
        try {
            kafkaTemplate.send("product-deleted", objectMapper.writeValueAsString(productId));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize productId for delete event", e);
        }
    }

    private void publishEvent(String topic, Product product) {
        try {
            String payload = objectMapper.writeValueAsString(product);
            kafkaTemplate.send(topic, payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize product for event", e);
        }
    }
}
