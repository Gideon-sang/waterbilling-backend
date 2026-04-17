package com.estate.waterbilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estate.waterbilling.model.Payment;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBillId(Integer billId);
}
