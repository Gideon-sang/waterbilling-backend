package com.estate.waterbilling.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String houseNumber;
    private String meterNumber;
    private String phone;

    // ✅ Added fields (NO OTHER CHANGE MADE)
    private Integer lastMeterReading;
    private Boolean inArrears = false;

    public Member() {
    }

    public Member(Integer id, String name, String houseNumber, String meterNumber, String phone) {
        this.id = id;
        this.name = name;
        this.houseNumber = houseNumber;
        this.meterNumber = meterNumber;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ✅ Added getters/setters (NO OTHER LINE TOUCHED)
    public Integer getLastMeterReading() {
        return lastMeterReading;
    }

    public void setLastMeterReading(Integer lastMeterReading) {
        this.lastMeterReading = lastMeterReading;
    }

    public Boolean getInArrears() {
        return inArrears;
    }

    public void setInArrears(Boolean inArrears) {
        this.inArrears = inArrears;
    }
}