package com.payment.service.payment_service.controller;

import com.payment.service.payment_service.model.Payment;
import com.payment.service.payment_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private final PaymentService paymentService;

    public EventController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Simple webhook-like endpoint to receive order events. Expects JSON like {"type":"OrderCreated","orderId":123,"amount":49.99}
    @PostMapping
    public ResponseEntity<String> receive(@RequestBody Map<String, Object> event) {
        String type = (String) event.get("type");
        if ("OrderCreated".equals(type)) {
            Number orderIdN = (Number) event.get("orderId");
            Number amountN = (Number) event.get("amount");
            if (orderIdN != null && amountN != null) {
                Payment p = new Payment();
                p.setOrderId(orderIdN.longValue());
                p.setAmount(BigDecimal.valueOf(amountN.doubleValue()));
                p.setPaymentStatus("COMPLETED");
                paymentService.createPayment(p);
                return ResponseEntity.ok("Payment initiated");
            }
            return ResponseEntity.badRequest().body("missing orderId or amount");
        }
        return ResponseEntity.ok("ignored");
    }
}
