package com.estate.waterbilling.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (reading.getMemberId() == null) {
            throw new RuntimeException("Member ID must be provided");
        }
        if (reading.getCurrentReading() == null) {
            throw new RuntimeException("Current reading is required");
        }

        Member member = memberRepository.findById(reading.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + reading.getMemberId()));

        reading.setMember(member);

        Integer previous = member.getLastMeterReading();
        boolean isFirstReading = (previous == null || previous <= 0);
        int previousReading = isFirstReading ? 0 : previous;

        reading.setPreviousReading(previousReading);

        if (reading.getCurrentReading() < previousReading) {
            throw new RuntimeException("Current reading (" + reading.getCurrentReading()
                    + ") cannot be less than previous reading (" + previousReading + ")");
        }

        reading.setUnitsUsed(reading.getCurrentReading() - previousReading);
        reading.setReadingDate(LocalDate.now());

        MeterReading savedReading = meterReadingRepository.save(reading);

        if (!isFirstReading) {
            double unpaidArrears = billRepository.findByMemberAndPaidFalse(member)
                    .stream()
                    .mapToDouble(Bill::getAmount)
                    .sum();

            double currentCharge = savedReading.getUnitsUsed() * RATE;
            double totalAmount = currentCharge + unpaidArrears;

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

            member.setInArrears(unpaidArrears > 0);
        }

        member.setLastMeterReading(savedReading.getCurrentReading());
        memberRepository.save(member);

        return savedReading;
    }

    // ✅ FIX 2: Update an existing meter reading and recalculate bill
    @Override
    @Transactional
    public MeterReading updateReading(Integer id, Integer newCurrentReading) {
        MeterReading reading = meterReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found with ID: " + id));

        if (newCurrentReading < reading.getPreviousReading()) {
            throw new RuntimeException("Current reading (" + newCurrentReading
                    + ") cannot be less than previous reading (" + reading.getPreviousReading() + ")");
        }

        // Update reading values
        reading.setCurrentReading(newCurrentReading);
        reading.setUnitsUsed(newCurrentReading - reading.getPreviousReading());
        MeterReading updatedReading = meterReadingRepository.save(reading);

        // Recalculate linked bill if it exists
        Bill bill = billRepository.findByReading(updatedReading);
        if (bill != null) {
            double currentCharge = updatedReading.getUnitsUsed() * RATE;
            double totalAmount = currentCharge + bill.getArrears();
            bill.setUnitsUsed(updatedReading.getUnitsUsed());
            bill.setAmount(totalAmount);
            billRepository.save(bill);
        }

        // Update member's last reading if this is their most recent one
        Member member = reading.getMember();
        MeterReading latestReading = meterReadingRepository
                .findTopByMember_IdOrderByReadingDateDesc(member.getId())
                .orElse(null);
        if (latestReading != null && latestReading.getId().equals(id)) {
            member.setLastMeterReading(newCurrentReading);
            memberRepository.save(member);
        }

        return updatedReading;
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