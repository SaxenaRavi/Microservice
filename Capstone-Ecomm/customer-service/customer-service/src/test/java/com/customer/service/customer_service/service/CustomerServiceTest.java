package com.customer.service.customer_service.service;

import com.customer.service.customer_service.domain.Customer;
import com.customer.service.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repo;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_savesCustomer() {
        Customer c = new Customer();
        c.setName("Alice");
        c.setEmail("alice@example.com");
        when(repo.save(c)).thenReturn(c);

        Customer res = service.create(c);

        assertSame(c, res);
        verify(repo).save(c);
    }

    @Test
    void findById_returnsCustomer() {
        Customer c = new Customer();
        c.setCustomerId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(c));

        Optional<Customer> res = service.findById(1L);

        assertTrue(res.isPresent());
        assertEquals(1L, res.get().getCustomerId());
    }

    @Test
    void update_existingCustomer_updatesFields() {
        Customer existing = new Customer();
        existing.setCustomerId(1L);
        existing.setName("Old");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        Customer updated = new Customer();
        updated.setName("New");

        Customer res = service.update(1L, updated);

        assertEquals("New", res.getName());
        verify(repo).save(existing);
    }

    @Test
    void delete_delegatesToRepository() {
        doNothing().when(repo).deleteById(1L);
        service.delete(1L);
        verify(repo).deleteById(1L);
    }
}
