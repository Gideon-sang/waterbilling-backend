package com.estate.waterbilling.controller;

import java.util.List;
import java.util.Map;

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
            return ResponseEntity.ok(meterReadingService.getReadingsByMember(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ FIX 2: Edit an existing meter reading
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReading(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        try {
            Integer newCurrentReading = body.get("currentReading");
            if (newCurrentReading == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currentReading is required");
            }
            MeterReading updated = meterReadingService.updateReading(id, newCurrentReading);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}