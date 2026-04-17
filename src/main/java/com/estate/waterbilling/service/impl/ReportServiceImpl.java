package com.estate.waterbilling.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberRepository memberRepository;

    // ✅ Get all members flagged with arrears
    @Override
    public List<Member> getMembersWithArrears() {
        return memberRepository.findByInArrears(true);
    }

    // ✅ Get all members up to date
    @Override
    public List<Member> getMembersUpToDate() {
        return memberRepository.findByInArrears(false);
    }
}
