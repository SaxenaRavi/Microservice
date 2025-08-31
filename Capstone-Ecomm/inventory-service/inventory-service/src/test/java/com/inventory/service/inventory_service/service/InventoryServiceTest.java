package com.inventory.service.inventory_service.service;

import com.inventory.service.inventory_service.model.Inventory;
import com.inventory.service.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    InventoryRepository repository;

    @InjectMocks
    InventoryService service;

    @Test
    void checkStock_whenPresent_returnsQuantity() {
        when(repository.findByProductId("p1")).thenReturn(Optional.of(new Inventory("p1", 5)));
        Optional<Integer> q = service.checkStock("p1");
        assertTrue(q.isPresent());
        assertEquals(5, q.get());
    }

    @Test
    void checkStock_whenMissing_returnsEmpty() {
        when(repository.findByProductId("p-x")).thenReturn(Optional.empty());
        Optional<Integer> q = service.checkStock("p-x");
        assertTrue(q.isEmpty());
    }

    @Test
    void updateStock_increaseNew_createsInventory() {
        when(repository.findByProductId("p2")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        boolean ok = service.updateStock("p2", 10);
        assertTrue(ok);

        ArgumentCaptor<Inventory> cap = ArgumentCaptor.forClass(Inventory.class);
        verify(repository).save(cap.capture());
        assertEquals("p2", cap.getValue().getProductId());
        assertEquals(10, cap.getValue().getQuantity());
    }

    @Test
    void updateStock_decrease_insufficient_returnsFalse() {
        when(repository.findByProductId("p3")).thenReturn(Optional.of(new Inventory("p3", 1)));
        boolean ok = service.updateStock("p3", -2);
        assertFalse(ok);
        verify(repository, never()).save(any());
    }
}
