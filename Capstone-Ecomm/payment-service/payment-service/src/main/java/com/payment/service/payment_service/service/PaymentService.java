package com.payment.service.payment_service.service;

import com.payment.service.payment_service.model.Payment;
import com.payment.service.payment_service.repository.PaymentRepository;
import com.payment.service.payment_service.exception.PaymentNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        payment.setPaymentDate(OffsetDateTime.now());
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus("PENDING");
        }
        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    public Payment updatePayment(Long id, Payment updated) {
        Payment existing = getPayment(id);
        // allow updating status and amount only for safety
        if (updated.getPaymentStatus() != null) existing.setPaymentStatus(updated.getPaymentStatus());
        if (updated.getAmount() != null) existing.setAmount(updated.getAmount());
        return paymentRepository.save(existing);
    }

    public void deletePayment(Long id) {
        Payment existing = getPayment(id);
        existing.setPaymentStatus("CANCELLED");
        paymentRepository.save(existing);
    }

}
