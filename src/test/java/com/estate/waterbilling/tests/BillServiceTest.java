package com.estate.waterbilling.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.impl.BillServiceImpl;

public class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BillServiceImpl billService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test get all bills
    @Test
    void testGetAllBills() {
        Bill b1 = new Bill();
        Bill b2 = new Bill();

        when(billRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<Bill> result = billService.getAllBills();

        assertEquals(2, result.size());
        System.out.println("✅ testGetAllBills passed");
    }

    // ✅ Test mark bill as paid
    @Test
    void testMarkAsPaid() {
        Member member = new Member();
        member.setId(1);
        member.setInArrears(true);

        Bill bill = new Bill();
        bill.setId(1);
        bill.setPaid(false);
        bill.setMember(member);

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));
        when(billRepository.findByMemberAndPaidFalse(member)).thenReturn(new ArrayList<>());
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Bill result = billService.markAsPaid(1);

        assertTrue(result.isPaid());
        assertFalse(member.getInArrears()); // arrears cleared
        System.out.println("✅ testMarkAsPaid passed");
    }

    // ✅ Test mark bill as paid — bill not found
    @Test
    void testMarkAsPaid_BillNotFound() {
        when(billRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            billService.markAsPaid(99);
        });

        assertTrue(ex.getMessage().contains("Bill not found"));
        System.out.println("✅ testMarkAsPaid_BillNotFound passed");
    }

    // ✅ Test get bills by member
    @Test
    void testGetBillsByMember() {
        Bill b1 = new Bill();
        Bill b2 = new Bill();

        when(billRepository.findByMemberId(1)).thenReturn(Arrays.asList(b1, b2));

        List<Bill> result = billService.getBillsByMember(1);

        assertEquals(2, result.size());
        System.out.println("✅ testGetBillsByMember passed");
    }
}