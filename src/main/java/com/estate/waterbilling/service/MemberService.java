package com.estate.waterbilling.service;


import java.util.List;
import com.estate.waterbilling.model.Member;

public interface MemberService {

    Member addMember(Member member);

    List<Member> getAllMembers();

    Member getMemberById(Integer id);

    Member updateMember(Integer id, Member member);

    void deleteMember(Integer id);

}
