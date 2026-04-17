package com.estate.waterbilling.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estate.waterbilling.model.MeterReading;
import com.estate.waterbilling.model.Member;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Integer> {

    // ✅ Get latest reading for a member
    Optional<MeterReading> findTopByMember_IdOrderByReadingDateDesc(Integer memberId);

    // ✅ Added - Get all readings for a member
    List<MeterReading> findByMember(Member member);

}
