package com.estate.waterbilling.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.MeterReading;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MeterReadingRepository;
import com.estate.waterbilling.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberById(Integer id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
    }

    @Override
    public Member updateMember(Integer id, Member member) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
        existingMember.setName(member.getName());
        existingMember.setHouseNumber(member.getHouseNumber());
        existingMember.setMeterNumber(member.getMeterNumber());
        existingMember.setPhone(member.getPhone());
        return memberRepository.save(existingMember);
    }

    // ✅ FIX 1: Delete bills and readings first to avoid foreign key constraint errors
    @Override
    @Transactional
    public void deleteMember(Integer id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));

        // Step 1: Delete all bills linked to this member
        List<Bill> bills = billRepository.findByMemberId(id);
        billRepository.deleteAll(bills);

        // Step 2: Delete all meter readings linked to this member
        List<MeterReading> readings = meterReadingRepository.findByMember(member);
        meterReadingRepository.deleteAll(readings);

        // Step 3: Now safe to delete the member
        memberRepository.deleteById(id);
    }
}