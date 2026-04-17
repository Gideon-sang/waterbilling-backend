package com.estate.waterbilling.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.MeterReading;
import com.estate.waterbilling.service.MeterReadingService;

@RestController
@RequestMapping("/meterReadings")
@CrossOrigin(origins = "*")
public class MeterReadingController {

    @Autowired
    private MeterReadingService meterReadingService;

    // ---------------- ADD NEW METER READING ----------------
    @PostMapping
    public ResponseEntity<?> addReading(@RequestBody MeterReading reading) {
        try {
            MeterReading saved = meterReadingService.addReading(reading);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ---------------- GET ALL READINGS ----------------
    @GetMapping
    public ResponseEntity<List<MeterReading>> getAllReadings() {
        return ResponseEntity.ok(meterReadingService.getAllReadings());
    }

    // ---------------- GET READING BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getReadingById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(meterReadingService.getReadingById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------- GET READINGS BY MEMBER ----------------
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getReadingsByMember(@PathVariable Integer memberId) {
        try {
            // ✅ Delegates to service properly instead of filtering in memory
            return ResponseEntity.ok(meterReadingService.getReadingsByMember(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}