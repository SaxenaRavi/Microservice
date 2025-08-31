package com.payment.service.payment_service.controller;

import com.payment.service.payment_service.model.Payment;
import com.payment.service.payment_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        Payment saved = paymentService.createPayment(payment);
        return ResponseEntity.created(URI.create("/payments/" + saved.getPaymentId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable("id") Long id, @RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.updatePayment(id, payment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
