package com.estate.waterbilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.Invoice;
import com.estate.waterbilling.service.InvoicePdfService;
import com.estate.waterbilling.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoicePdfService invoicePdfService; // ✅ Added

    // ✅ Generate invoice for a member
    @PostMapping("/generate/{memberId}")
    public ResponseEntity<?> generateInvoice(@PathVariable Integer memberId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(invoiceService.generateInvoice(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Get all invoices
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    // ✅ Get invoices by member
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getInvoicesByMember(@PathVariable Integer memberId) {
        try {
            return ResponseEntity.ok(invoiceService.getInvoicesByMember(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Download PDF invoice for a member
    @GetMapping("/pdf/{memberId}")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Integer memberId) {
        try {
            byte[] pdf = invoicePdfService.generateInvoicePdf(memberId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "invoice-member-" + memberId + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
