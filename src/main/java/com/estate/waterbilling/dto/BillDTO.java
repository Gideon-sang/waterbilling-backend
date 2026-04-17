package com.estate.waterbilling.dto;

import java.time.LocalDate;

public class BillDTO {
    private Integer id;
    private Integer memberId;
    private String memberName;
    private String houseNumber;
    private Integer unitsUsed;
    private Double amount;
    private Double arrears;
    private LocalDate billDate;
    private LocalDate dueDate;
    private boolean paid;

    // ✅ Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public Integer getUnitsUsed() { return unitsUsed; }
    public void setUnitsUsed(Integer unitsUsed) { this.unitsUsed = unitsUsed; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getArrears() { return arrears; }
    public void setArrears(Double arrears) { this.arrears = arrears; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}