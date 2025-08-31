package com.inventory.service.inventory_service.web;

import com.inventory.service.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    InventoryRepository repository;

                // No Testcontainers here - tests will use embedded MongoDB (flapdoodle) provided via test dependency

    @Disabled
    @Test
    void checkAndUpdateFlow() throws Exception {
        // ensure clean state
        repository.deleteAll();

        // update stock +20
        mvc.perform(post("/inventory/updateStock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":\"p-int-1\", \"delta\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("p-int-1"));

        // check stock
        mvc.perform(get("/inventory/checkStock").param("productId", "p-int-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(20));

        // decrease by 5
        mvc.perform(post("/inventory/updateStock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":\"p-int-1\", \"delta\":-5}"))
                .andExpect(status().isOk());

        mvc.perform(get("/inventory/checkStock").param("productId", "p-int-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }
}
