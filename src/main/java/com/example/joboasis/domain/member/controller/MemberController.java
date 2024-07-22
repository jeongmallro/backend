package com.example.joboasis.domain.member.controller;

import com.example.joboasis.domain.member.dto.MemberRequestDto;
import com.example.joboasis.domain.member.dto.MemberResponseDto;
import com.example.joboasis.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.checkEmail(email);
    }

    @GetMapping("/check-id")
    public boolean checkId(@RequestParam String loginId) {
        return memberService.checkId(loginId);
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> addMember(@RequestBody MemberRequestDto memberDto) {
        MemberResponseDto newMemberDto = memberService.addMember(memberDto);
        return new ResponseEntity<>(newMemberDto, HttpStatus.CREATED);
    }
}
