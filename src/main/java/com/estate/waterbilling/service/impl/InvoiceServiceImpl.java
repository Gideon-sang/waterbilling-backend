package com.estate.waterbilling.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Invoice;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.InvoiceRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillRepository billRepository;

    @Override
    public Invoice generateInvoice(Integer memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        List<Bill> allBills = billRepository.findByMemberId(memberId);

        if (allBills.isEmpty()) {
            throw new RuntimeException("No bills found for member ID: " + memberId);
        }

        // ✅ Current charges — latest unpaid bill only
        Bill latestBill = billRepository.findTopByMemberIdOrderByBillDateDesc(memberId);
        double currentCharges = (latestBill != null && !latestBill.isPaid())
                ? latestBill.getAmount() : 0.0;

        // ✅ Arrears — all unpaid bills except latest
        double arrears = allBills.stream()
                .filter(b -> !b.isPaid() && !b.getId().equals(latestBill != null ? latestBill.getId() : null))
                .mapToDouble(Bill::getAmount)
                .sum();

        // ✅ Total = current charges + arrears
        double totalAmount = currentCharges + arrears;

        // ✅ Generate unique invoice number e.g INV-2026-04-001
        String invoiceNumber = "INV-" + LocalDate.now().getYear()
                + "-" + String.format("%02d", LocalDate.now().getMonthValue())
                + "-" + String.format("%03d", memberId);

        Invoice invoice = new Invoice();
        invoice.setMember(member);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCurrentCharges(currentCharges);
        invoice.setArrears(arrears);
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDueDate(LocalDate.now().plusDays(30));

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> getInvoicesByMember(Integer memberId) {
        return invoiceRepository.findByMemberId(memberId);
    }
}