package com.estate.waterbilling.service;

import com.estate.waterbilling.model.Payment;
import java.util.List;

public interface PaymentService {
    Payment payBill(Integer billId, Double amountPaid);
    List<Payment> getPaymentsByBill(Integer billId);
    List<Payment> getAllPayments();
}
