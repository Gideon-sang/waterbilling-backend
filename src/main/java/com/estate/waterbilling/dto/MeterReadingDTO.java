package com.estate.waterbilling.dto;

import java.time.LocalDate;

public class MeterReadingDTO {
    private Integer id;
    private Integer memberId;
    private String memberName;
    private String houseNumber;
    private Integer previousReading;
    private Integer currentReading;
    private Integer unitsUsed;
    private LocalDate readingDate;

    // ✅ Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public Integer getPreviousReading() { return previousReading; }
    public void setPreviousReading(Integer previousReading) { this.previousReading = previousReading; }

    public Integer getCurrentReading() { return currentReading; }
    public void setCurrentReading(Integer currentReading) { this.currentReading = currentReading; }

    public Integer getUnitsUsed() { return unitsUsed; }
    public void setUnitsUsed(Integer unitsUsed) { this.unitsUsed = unitsUsed; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }
}
