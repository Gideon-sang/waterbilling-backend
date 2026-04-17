package com.estate.waterbilling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estate.waterbilling.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    // ✅ Added - Get members by arrears status
    List<Member> findByInArrears(Boolean inArrears);

}
