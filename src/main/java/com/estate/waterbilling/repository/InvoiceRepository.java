package com.estate.waterbilling.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.estate.waterbilling.model.Invoice;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByMemberId(Integer memberId);
}

