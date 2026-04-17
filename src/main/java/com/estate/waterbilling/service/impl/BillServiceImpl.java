package com.estate.waterbilling.service.impl;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.BillService;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getBillsByMember(Integer memberId) {
        return billRepository.findByMemberId(memberId);
    }

    @Override
    public Bill markAsPaid(Integer billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

        bill.setPaid(true);
        billRepository.save(bill);

        // ✅ Correctly fetch only UNPAID bills to check arrears
        Member member = bill.getMember();
        List<Bill> unpaidBills = billRepository.findByMemberAndPaidFalse(member);
        
        // ✅ If no unpaid bills remain, clear arrears flag
        member.setInArrears(!unpaidBills.isEmpty());
        memberRepository.save(member);

        return bill;
    }

    @Override
    public List<Bill> getOverdueBills(int months) {
        LocalDate limit = LocalDate.now().minusMonths(months);
        return billRepository.findByPaidFalseAndBillDateBefore(limit);
    }
}