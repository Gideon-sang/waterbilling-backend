package com.estate.waterbilling.dto;

public class MemberDTO {
    private Integer id;
    private String name;
    private String houseNumber;
    private String meterNumber;
    private String phone;
    private Integer lastMeterReading;
    private Boolean inArrears;

    // ✅ Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getLastMeterReading() { return lastMeterReading; }
    public void setLastMeterReading(Integer lastMeterReading) { this.lastMeterReading = lastMeterReading; }

    public Boolean getInArrears() { return inArrears; }
    public void setInArrears(Boolean inArrears) { this.inArrears = inArrears; }
}
