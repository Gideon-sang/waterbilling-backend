package com.estate.waterbilling.service;

import java.util.List;
import com.estate.waterbilling.model.MeterReading;

public interface MeterReadingService {

    MeterReading addReading(MeterReading reading);

    List<MeterReading> getAllReadings();

    MeterReading getReadingById(Integer id);

    List<MeterReading> getReadingsByMember(Integer memberId);

    // ✅ FIX 2: New method to update an existing reading
    MeterReading updateReading(Integer id, Integer newCurrentReading);
}