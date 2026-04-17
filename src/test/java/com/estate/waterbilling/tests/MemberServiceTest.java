package com.estate.waterbilling.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.MemberRepository;
import com.estate.waterbilling.service.impl.MemberServiceImpl;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test add member
    @Test
    void testAddMember() {
        Member member = new Member();
        member.setName("John Doe");
        member.setHouseNumber("H01");
        member.setMeterNumber("MTR001");
        member.setPhone("0712345678");

        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.addMember(member);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("H01", result.getHouseNumber());
        System.out.println("✅ testAddMember passed");
    }

    // ✅ Test get all members
    @Test
    void testGetAllMembers() {
        Member m1 = new Member();
        m1.setName("Peter Mwangi");

        Member m2 = new Member();
        m2.setName("Jane Wanjiru");

        when(memberRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Member> result = memberService.getAllMembers();

        assertEquals(2, result.size());
        System.out.println("✅ testGetAllMembers passed");
    }

    // ✅ Test get member by ID — found
    @Test
    void testGetMemberById_Found() {
        Member member = new Member();
        member.setId(1);
        member.setName("Peter Mwangi");

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1);

        assertNotNull(result);
        assertEquals("Peter Mwangi", result.getName());
        System.out.println("✅ testGetMemberById_Found passed");
    }

    // ✅ Test get member by ID — not found
    @Test
    void testGetMemberById_NotFound() {
        when(memberRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            memberService.getMemberById(99);
        });

        assertTrue(ex.getMessage().contains("Member not found"));
        System.out.println("✅ testGetMemberById_NotFound passed");
    }

    // ✅ Test delete member
    @Test
    void testDeleteMember() {
        Member member = new Member();
        member.setId(1);

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        doNothing().when(memberRepository).deleteById(1);

        assertDoesNotThrow(() -> memberService.deleteMember(1));
        System.out.println("✅ testDeleteMember passed");
    }
}