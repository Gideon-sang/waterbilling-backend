package com.estate.waterbilling.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.MeterReading;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MeterReadingRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.MeterReadingService;

@Service
public class MeterReadingServiceImpl implements MeterReadingService {

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillRepository billRepository;

    private static final double RATE = 100.0;

    @Override
    public MeterReading addReading(MeterReading reading) {

        // ✅ STEP 1: Validate inputs
        if (reading.getMemberId() == null) {
            throw new RuntimeException("Member ID must be provided");
        }

        if (reading.getCurrentReading() == null) {
            throw new RuntimeException("Current reading is required");
        }

        // ✅ STEP 2: Fetch member from DB
        Member member = memberRepository.findById(reading.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + reading.getMemberId()));

        // ✅ STEP 3: Attach member to reading
        reading.setMember(member);

        // ✅ STEP 4: Determine previous reading
        Integer previous = member.getLastMeterReading();
        boolean isFirstReading = (previous == null || previous <= 0);
        int previousReading = isFirstReading ? 0 : previous;

        reading.setPreviousReading(previousReading);

        // ✅ STEP 5: Validate current reading is not less than previous
        if (reading.getCurrentReading() < previousReading) {
            throw new RuntimeException("Current reading (" + reading.getCurrentReading()
                    + ") cannot be less than previous reading (" + previousReading + ")");
        }

        // ✅ STEP 6: Auto-calculate units used
        reading.setUnitsUsed(reading.getCurrentReading() - previousReading);
        reading.setReadingDate(LocalDate.now());

        // ✅ STEP 7: Save reading
        MeterReading savedReading = meterReadingRepository.save(reading);

        // ✅ STEP 8: Auto-generate bill (only from 2nd reading onwards)
        if (!isFirstReading) {

            // Calculate unpaid arrears from previous bills
            double unpaidArrears = billRepository.findByMemberAndPaidFalse(member)
                    .stream()
                    .mapToDouble(Bill::getAmount)
                    .sum();

            double currentCharge = savedReading.getUnitsUsed() * RATE;
            double totalAmount = currentCharge + unpaidArrears;

            // ✅ STEP 9: Create and save bill
            Bill bill = new Bill();
            bill.setMember(member);
            bill.setReading(savedReading);
            bill.setUnitsUsed(savedReading.getUnitsUsed());
            bill.setArrears(unpaidArrears);
            bill.setAmount(totalAmount);
            bill.setPaid(false);
            bill.setBillDate(LocalDate.now());
            bill.setDueDate(LocalDate.now().plusDays(30));

            billRepository.save(bill);

            // ✅ STEP 10: Flag member as in arrears if they have unpaid bills
            member.setInArrears(unpaidArrears > 0);
        }

        // ✅ STEP 11: Always update member's last meter reading
        member.setLastMeterReading(savedReading.getCurrentReading());
        memberRepository.save(member);

        return savedReading;
    }

    @Override
    public List<MeterReading> getAllReadings() {
        return meterReadingRepository.findAll();
    }

    @Override
    public MeterReading getReadingById(Integer id) {
        return meterReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found with ID: " + id));
    }

    @Override
    public List<MeterReading> getReadingsByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
        return meterReadingRepository.findByMember(member);
    }
}

