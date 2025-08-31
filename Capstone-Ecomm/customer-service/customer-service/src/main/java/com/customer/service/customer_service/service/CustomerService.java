package com.customer.service.customer_service.service;

import com.customer.service.customer_service.domain.Customer;
import com.customer.service.customer_service.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer create(Customer customer) {
        return repo.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return repo.findById(id);
    }

    public List<Customer> findAll() {
        return repo.findAll();
    }

    public Customer update(Long id, Customer updated) {
        return repo.findById(id).map(c -> {
            c.setName(updated.getName());
            c.setEmail(updated.getEmail());
            c.setAddress(updated.getAddress());
            c.setPhoneNumber(updated.getPhoneNumber());
            return repo.save(c);
        }).orElseGet(() -> {
            updated.setCustomerId(id);
            return repo.save(updated);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
