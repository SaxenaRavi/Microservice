package com.order.processing.service.controller;

import com.order.processing.service.model.Order;
import com.order.processing.service.service.OrderService;
import com.order.processing.service.controller.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

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

        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setStatus("PENDING");

        when(orderService.createOrder(order)).thenReturn(createdOrder);

        // Act
        ResponseEntity<Order> response = orderController.createOrder(order);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdOrder, response.getBody());
        verify(orderService, times(1)).createOrder(order);
    }

    @Test
    void testCancelOrder() {
        // Arrange
        Long orderId = 1L;

        // Act
        ResponseEntity<String> response = orderController.cancelOrder(orderId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order cancelled successfully", response.getBody());
        verify(orderService, times(1)).cancelOrder(orderId);
    }
}
