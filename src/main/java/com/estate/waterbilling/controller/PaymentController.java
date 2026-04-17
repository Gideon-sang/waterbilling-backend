package com.estate.waterbilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.Payment;
import com.estate.waterbilling.service.PaymentService;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{billId}")
    public Payment payBill(@PathVariable Integer billId, @RequestParam Double amountPaid) {
        return paymentService.payBill(billId, amountPaid);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/bill/{billId}")
    public List<Payment> getPaymentsByBill(@PathVariable Integer billId) {
        return paymentService.getPaymentsByBill(billId);
    }
}
