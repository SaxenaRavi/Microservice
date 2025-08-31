package com.inventory.service.inventory_service.service;

import com.inventory.service.inventory_service.model.Inventory;
import com.inventory.service.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public Optional<Integer> checkStock(String productId) {
        return repository.findByProductId(productId).map(Inventory::getQuantity);
    }

    public boolean updateStock(String productId, int delta) {
        Optional<Inventory> maybe = repository.findByProductId(productId);
        Inventory inv;
        if (maybe.isPresent()) {
            inv = maybe.get();
            int newQty = inv.getQuantity() + delta;
            if (newQty < 0) return false;
            inv.setQuantity(newQty);
        } else {
            if (delta < 0) return false;
            inv = new Inventory(productId, delta);
        }
        repository.save(inv);
        return true;
    }
}
