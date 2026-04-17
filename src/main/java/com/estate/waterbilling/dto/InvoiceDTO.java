package com.estate.waterbilling.dto;

import java.time.LocalDate;

public class InvoiceDTO {
    private Integer id;
    private Integer memberId;
    private String memberName;
    private String houseNumber;
    private String meterNumber;
    private String invoiceNumber;
    private Double currentCharges;
    private Double arrears;
    private Double totalAmount;
    private LocalDate invoiceDate;
    private LocalDate dueDate;

    // ✅ Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Double getCurrentCharges() { return currentCharges; }
    public void setCurrentCharges(Double currentCharges) { this.currentCharges = currentCharges; }

    public Double getArrears() { return arrears; }
    public void setArrears(Double arrears) { this.arrears = arrears; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
