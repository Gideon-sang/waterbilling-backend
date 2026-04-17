package com.estate.waterbilling.service;
import java.util.List;
import com.estate.waterbilling.model.MeterReading;

public interface MeterReadingService {

    MeterReading addReading(MeterReading reading);

    List<MeterReading> getAllReadings();

    MeterReading getReadingById(Integer id);

    List<MeterReading> getReadingsByMember(Integer memberId); // ✅ Added

}