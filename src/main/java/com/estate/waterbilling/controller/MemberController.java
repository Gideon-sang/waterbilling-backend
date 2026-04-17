package com.estate.waterbilling.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.service.MemberService;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // ✅ Add new member
    @PostMapping
    public ResponseEntity<?> addMember(@RequestBody Member member) {
        try {
            if (member.getInArrears() == null) member.setInArrears(false);
            if (member.getLastMeterReading() == null) member.setLastMeterReading(0);
            return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(member));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Get all members
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // ✅ Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(memberService.getMemberById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Update member
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Integer id, @RequestBody Member member) {
        try {
            return ResponseEntity.ok(memberService.updateMember(id, member));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Integer id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("Member deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}