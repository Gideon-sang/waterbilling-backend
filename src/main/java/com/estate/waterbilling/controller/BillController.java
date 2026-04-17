package com.estate.waterbilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.service.BillService;

@RestController
@RequestMapping("/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillService billService;

    // ✅ Get all bills
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    // ✅ Get all bills for one member
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getBillsByMember(@PathVariable Integer memberId) {
        try {
            return ResponseEntity.ok(billService.getBillsByMember(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Mark bill as paid
    @PutMapping("/pay/{billId}")
    public ResponseEntity<?> markAsPaid(@PathVariable Integer billId) {
        try {
            return ResponseEntity.ok(billService.markAsPaid(billId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Get overdue bills
    @GetMapping("/overdue/{months}")
    public ResponseEntity<List<Bill>> getOverdueBills(@PathVariable int months) {
        return ResponseEntity.ok(billService.getOverdueBills(months));
    }
}