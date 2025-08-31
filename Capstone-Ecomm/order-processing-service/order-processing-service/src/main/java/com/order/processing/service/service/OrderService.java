package com.order.processing.service.service;

import com.order.processing.service.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order createOrder(Order order) {
        // Generate order ID
        order.setId(orderIdGenerator.getAndIncrement());
        order.setStatus("PENDING");

        // Send message to Inventory service to check stock
        rabbitTemplate.convertAndSend("inventory.exchange", "inventory.check", order);

        // Send message to Payment service to process payment
        rabbitTemplate.convertAndSend("payment.exchange", "payment.process", order);

        return order;
    }

    public void cancelOrder(Long orderId) {
        // Send message to Inventory service to release stock
        rabbitTemplate.convertAndSend("inventory.exchange", "inventory.release", orderId);

        // Send message to Payment service to refund if necessary
        rabbitTemplate.convertAndSend("payment.exchange", "payment.refund", orderId);
    }
}
