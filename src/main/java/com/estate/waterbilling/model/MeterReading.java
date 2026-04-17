package com.estate.waterbilling.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer previousReading;
    private Integer currentReading;
    private Integer unitsUsed;
    private LocalDate readingDate;

    // Used only for JSON input { "memberId": 3, "currentReading": 200 }
    @Transient
    private Integer memberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @OneToOne(mappedBy = "reading")
    @JsonIgnore
    private Bill bill; 

    public MeterReading() {}

    public MeterReading(Integer id, Integer previousReading, Integer currentReading, Integer unitsUsed, LocalDate readingDate, Member member) {
        this.id = id;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
        this.unitsUsed = unitsUsed;
        this.readingDate = readingDate;
        this.member = member;
    }

    // ---------------- GETTERS & SETTERS ----------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(Integer previousReading) {
        this.previousReading = previousReading;
    }

    public Integer getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(Integer currentReading) {
        this.currentReading = currentReading;

        // Auto-calculate unitsUsed when both readings available
        if (this.previousReading != null && currentReading != null) {
            this.unitsUsed = currentReading - this.previousReading;
        }
    }

    public Integer getUnitsUsed() {
        return unitsUsed;
    }

    public void setUnitsUsed(Integer unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDate readingDate) {
        this.readingDate = readingDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;

        // Auto-set previousReading using member.lastMeterReading
        if (member != null && member.getLastMeterReading() != null) {
            this.previousReading = member.getLastMeterReading();
        }
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    // JSON support
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}