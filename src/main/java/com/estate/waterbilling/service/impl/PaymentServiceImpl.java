package com.estate.waterbilling.service.impl;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Payment;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.PaymentRepository;
import com.estate.waterbilling.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Override
    public Payment payBill(Integer billId, Double amountPaid) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        double remainingArrears = bill.getArrears() - amountPaid;
        bill.setArrears(Math.max(0, remainingArrears));

        if (amountPaid >= bill.getAmount() + bill.getArrears()) {
            bill.setPaid(true);
        }

        billRepository.save(bill);

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentDate(LocalDate.now());

        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByBill(Integer billId) {
        return paymentRepository.findByBillId(billId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}