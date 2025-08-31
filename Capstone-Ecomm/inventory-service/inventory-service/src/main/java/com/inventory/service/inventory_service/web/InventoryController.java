package com.inventory.service.inventory_service.web;

import com.inventory.service.inventory_service.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/checkStock")
    public ResponseEntity<?> checkStock(@RequestParam String productId) {
        return service.checkStock(productId)
                .map(q -> ResponseEntity.ok(Map.of("productId", productId, "quantity", q)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "product not found")));
    }

    @PostMapping("/updateStock")
    public ResponseEntity<?> updateStock(@RequestBody Map<String, Object> body) {
        String productId = (String) body.get("productId");
        Object d = body.get("delta");
        if (productId == null || d == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "productId and delta required"));
        }
        int delta;
        try {
            delta = Integer.parseInt(d.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "delta must be an integer"));
        }
        boolean ok = service.updateStock(productId, delta);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("error", "insufficient stock or invalid operation"));
        }
        return ResponseEntity.ok(Map.of("productId", productId, "delta", delta));
    }
}
