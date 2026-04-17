package com.estate.waterbilling.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Member;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    // Fetch all bills for a specific member by member ID
    List<Bill> findByMemberId(Integer memberId);

    // Check if a bill already exists for a member for a given month
    boolean existsByMemberIdAndBillDate(Integer memberId, LocalDate billDate);

    // Fetch overdue/unpaid bills before a specific date
    List<Bill> findByPaidFalseAndBillDateBefore(LocalDate date);

    // Fetch the latest bill for a member (needed for auto-calculation)
    Bill findTopByMemberIdOrderByBillDateDesc(Integer memberId);

    // Fetch all unpaid bills for a member (needed for arrears calculation)
    List<Bill> findByMemberAndPaidFalse(Member member);
}