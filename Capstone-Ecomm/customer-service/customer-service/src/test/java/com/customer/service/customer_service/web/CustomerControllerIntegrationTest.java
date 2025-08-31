package com.customer.service.customer_service.web;

import com.customer.service.customer_service.CustomerServiceApplication;
import com.customer.service.customer_service.domain.Customer;
import com.customer.service.customer_service.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CustomerServiceApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository repo;

    @Test
    void createAndGetCustomer() throws Exception {
        Customer c = new Customer();
        c.setName("Bob");
        c.setEmail("bob@example.com");

        String body = mapper.writeValueAsString(c);

        String created = mvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Customer res = mapper.readValue(created, Customer.class);

        mvc.perform(get("/customers/" + res.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }
}
