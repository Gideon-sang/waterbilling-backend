package com.estate.waterbilling.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

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
        // ✅ Throw proper error instead of returning null
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
    }

    @Override
    public Member updateMember(Integer id, Member member) {
        // ✅ Throw proper error if member not found
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));

        existingMember.setName(member.getName());
        existingMember.setHouseNumber(member.getHouseNumber());
        existingMember.setMeterNumber(member.getMeterNumber());
        existingMember.setPhone(member.getPhone());

        return memberRepository.save(existingMember);
    }

    @Override
    public void deleteMember(Integer id) {
        // ✅ Check member exists before deleting
        memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
        memberRepository.deleteById(id);
    }
}

