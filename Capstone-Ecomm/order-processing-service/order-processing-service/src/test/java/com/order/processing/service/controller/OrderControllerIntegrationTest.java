package com.order.processing.service.controller;

import com.order.processing.service.model.Order;
import com.order.processing.service.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class OrderControllerIntegrationTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCreateOrderEndpoint() throws Exception {
        mockMvc.perform(post("/api/orders/createOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":\"customer1\",\"items\":[{\"productId\":1,\"productName\":\"Product A\",\"quantity\":2,\"price\":50.0}],\"totalAmount\":100.0,\"status\":\"NEW\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCancelOrderEndpoint() throws Exception {
        mockMvc.perform(post("/api/orders/cancelOrder")
                .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));
    }
}
