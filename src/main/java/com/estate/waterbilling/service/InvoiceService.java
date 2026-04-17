package com.estate.waterbilling.service;
import com.estate.waterbilling.model.Invoice;
import java.util.List;

public interface InvoiceService {
    Invoice generateInvoice(Integer memberId);
    List<Invoice> getAllInvoices();
    List<Invoice> getInvoicesByMember(Integer memberId);
}
