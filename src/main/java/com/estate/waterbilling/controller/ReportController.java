package com.estate.waterbilling.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.service.ReportPdfService;
import com.estate.waterbilling.service.ReportService;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportPdfService reportPdfService;

    // ✅ Get all members with arrears (JSON)
    @GetMapping("/arrears")
    public ResponseEntity<List<Member>> getMembersWithArrears() {
        return ResponseEntity.ok(reportService.getMembersWithArrears());
    }

    // ✅ Get all members up to date (JSON)
    @GetMapping("/uptodate")
    public ResponseEntity<List<Member>> getMembersUpToDate() {
        return ResponseEntity.ok(reportService.getMembersUpToDate());
    }

    // ✅ Download arrears report as PDF
    @GetMapping("/arrears/pdf")
    public ResponseEntity<byte[]> downloadArrearsReport() {
        try {
            byte[] pdf = reportPdfService.generateArrearsReportPdf();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "arrears-report.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}