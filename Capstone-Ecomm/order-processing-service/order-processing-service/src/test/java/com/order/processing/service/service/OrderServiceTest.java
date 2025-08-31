package com.order.processing.service.service;

import com.order.processing.service.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        // Arrange
        Order order = new Order();
        order.setCustomerId("customer1");
        order.setTotalAmount(BigDecimal.valueOf(100.0));
        order.setStatus("NEW");

        // Act
        Order createdOrder = orderService.createOrder(order);

        // Assert
        assertNotNull(createdOrder.getId());
        assertEquals("PENDING", createdOrder.getStatus());
        verify(rabbitTemplate, times(1)).convertAndSend("inventory.exchange", "inventory.check", createdOrder);
        verify(rabbitTemplate, times(1)).convertAndSend("payment.exchange", "payment.process", createdOrder);
    }

    @Test
    void testCancelOrder() {
        // Arrange
        Long orderId = 1L;

        // Act
        orderService.cancelOrder(orderId);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend("inventory.exchange", "inventory.release", orderId);
        verify(rabbitTemplate, times(1)).convertAndSend("payment.exchange", "payment.refund", orderId);
    }
}
