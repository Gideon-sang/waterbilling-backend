package com.estate.waterbilling.model;
import java.time.LocalDate;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bill") // ✅ removed uniqueConstraints
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ---------------- MEMBER ----------------
    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    // ---------------- READING ----------------
    @OneToOne
    @JoinColumn(name = "reading_id", unique = true)
    @JsonIgnore
    private MeterReading reading;

    private Integer unitsUsed;
    private Double amount;
    private LocalDate billDate;
    private LocalDate dueDate;

    private boolean paid = false;
    private Double arrears = 0.0;

    // ---------------- GETTERS & SETTERS ----------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MeterReading getReading() {
        return reading;
    }

    public void setReading(MeterReading reading) {
        this.reading = reading;
    }

    public Integer getUnitsUsed() {
        return unitsUsed;
    }

    public void setUnitsUsed(Integer unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }
}