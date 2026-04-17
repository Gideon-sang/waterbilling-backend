package com.estate.waterbilling.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.model.MeterReading;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MeterReadingRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.impl.MeterReadingServiceImpl;

public class MeterReadingServiceTest {

    @Mock
    private MeterReadingRepository meterReadingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private MeterReadingServiceImpl meterReadingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test first reading — no bill generated
    @Test
    void testFirstReading_NoBillGenerated() {
        Member member = new Member();
        member.setId(1);
        member.setName("Peter Mwangi");
        member.setLastMeterReading(null); // first reading

        MeterReading reading = new MeterReading();
        reading.setMemberId(1);
        reading.setCurrentReading(100);

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(meterReadingRepository.save(any(MeterReading.class))).thenAnswer(i -> i.getArguments()[0]);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MeterReading result = meterReadingService.addReading(reading);

        assertNotNull(result);
        assertEquals(100, result.getCurrentReading());
        assertEquals(0, result.getPreviousReading());
        assertEquals(100, result.getUnitsUsed());

        // Bill should NOT be generated for first reading
        verify(billRepository, never()).save(any());
        System.out.println("✅ testFirstReading_NoBillGenerated passed");
    }

    // ✅ Test second reading — bill generated
    @Test
    void testSecondReading_BillGenerated() {
        Member member = new Member();
        member.setId(1);
        member.setName("Peter Mwangi");
        member.setLastMeterReading(100); // has previous reading

        MeterReading reading = new MeterReading();
        reading.setMemberId(1);
        reading.setCurrentReading(150);

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(meterReadingRepository.save(any(MeterReading.class))).thenAnswer(i -> i.getArguments()[0]);
        when(billRepository.findByMemberAndPaidFalse(member)).thenReturn(new ArrayList<>());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MeterReading result = meterReadingService.addReading(reading);

        assertEquals(50, result.getUnitsUsed());
        assertEquals(100, result.getPreviousReading());
        assertEquals(150, result.getCurrentReading());

        // Bill SHOULD be generated
        verify(billRepository, times(1)).save(any());
        System.out.println("✅ testSecondReading_BillGenerated passed");
    }

    // ✅ Test missing member ID
    @Test
    void testAddReading_MissingMemberId() {
        MeterReading reading = new MeterReading();
        reading.setCurrentReading(100);
        // memberId not set

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            meterReadingService.addReading(reading);
        });

        assertTrue(ex.getMessage().contains("Member ID must be provided"));
        System.out.println("✅ testAddReading_MissingMemberId passed");
    }

    // ✅ Test current reading less than previous
    @Test
    void testAddReading_CurrentLessThanPrevious() {
        Member member = new Member();
        member.setId(1);
        member.setLastMeterReading(200);

        MeterReading reading = new MeterReading();
        reading.setMemberId(1);
        reading.setCurrentReading(150); // less than previous

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            meterReadingService.addReading(reading);
        });

        assertTrue(ex.getMessage().contains("cannot be less than previous reading"));
        System.out.println("✅ testAddReading_CurrentLessThanPrevious passed");
    }
}