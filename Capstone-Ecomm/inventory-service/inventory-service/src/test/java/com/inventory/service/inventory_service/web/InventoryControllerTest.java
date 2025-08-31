package com.inventory.service.inventory_service.web;

import com.inventory.service.inventory_service.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InventoryController.class)
class InventoryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    InventoryService service;

    @Test
    void checkStockFound() throws Exception {
        when(service.checkStock("p-1")).thenReturn(Optional.of(12));

        mvc.perform(get("/inventory/checkStock").param("productId", "p-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("p-1"))
                .andExpect(jsonPath("$.quantity").value(12));
    }

    @Test
    void checkStockNotFound() throws Exception {
        when(service.checkStock(anyString())).thenReturn(Optional.empty());

        mvc.perform(get("/inventory/checkStock").param("productId", "missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("product not found"));
    }

    @Test
    void updateStockBadRequest() throws Exception {
        mvc.perform(post("/inventory/updateStock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":\"p-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStockOk() throws Exception {
        when(service.updateStock("p-1", 5)).thenReturn(true);

        mvc.perform(post("/inventory/updateStock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":\"p-1\", \"delta\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("p-1"))
                .andExpect(jsonPath("$.delta").value(5));
    }
}
