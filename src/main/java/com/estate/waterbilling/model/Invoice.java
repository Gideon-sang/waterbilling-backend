package com.estate.waterbilling.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate invoiceDate;
    private Double totalAmount;
    private Double arrears;
    private Double currentCharges; // ✅ Added - current month charges only
    private String invoiceNumber;  // ✅ Added - unique reference e.g INV-001
    private LocalDate dueDate;     // ✅ Added - payment due date

    public Invoice() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getArrears() { return arrears; }
    public void setArrears(Double arrears) { this.arrears = arrears; }

    public Double getCurrentCharges() { return currentCharges; }
    public void setCurrentCharges(Double currentCharges) { this.currentCharges = currentCharges; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
