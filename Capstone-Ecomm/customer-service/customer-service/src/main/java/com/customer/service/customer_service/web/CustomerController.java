package com.customer.service.customer_service.web;

import com.customer.service.customer_service.domain.Customer;
import com.customer.service.customer_service.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer created = service.create(customer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable Long customerId) {
        return service.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable Long customerId, @RequestBody Customer customer) {
        Customer updated = service.update(customerId, customer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        service.delete(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Stub for fetching orders for a customer - in a real system we'd call order service.
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<Object> getOrdersForCustomer(@PathVariable Long customerId) {
        // Minimal stub: return a placeholder JSON with customerId and empty orders list
        return ResponseEntity.ok(java.util.Map.of("customerId", customerId, "orders", java.util.List.of()));
    }
}
