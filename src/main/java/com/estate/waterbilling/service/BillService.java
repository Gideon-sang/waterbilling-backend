package com.estate.waterbilling.service;
import java.util.List;
import com.estate.waterbilling.model.Bill;

public interface BillService {

    // ❌ Removed manual generation methods
    // Bill generateBill(Integer memberId, Integer unitsUsed);
    // Bill generateBillFromReading(Integer memberId, Integer newReading);

    List<Bill> getAllBills();

    List<Bill> getBillsByMember(Integer memberId);

    Bill markAsPaid(Integer billId);

    List<Bill> getOverdueBills(int months);
}