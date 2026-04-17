package com.estate.waterbilling.service;

import com.estate.waterbilling.model.Member;
import java.util.List;

public interface ReportService {

    // ✅ Get all members with arrears
    List<Member> getMembersWithArrears();

    // ✅ Get all members up to date
    List<Member> getMembersUpToDate();
}
