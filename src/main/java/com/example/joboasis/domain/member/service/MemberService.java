package com.example.joboasis.domain.member.service;

import com.example.joboasis.domain.member.dto.MemberRequestDto;
import com.example.joboasis.domain.member.dto.MemberResponseDto;
import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional
    public MemberResponseDto addMember(MemberRequestDto memberDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(memberDto.getPassword());
        Member member = memberDto.toEntity(encodedPassword);
        Member savedMember = memberRepository.save(member);
        log.info("memberId={}", savedMember.getId());

        return savedMember.toResponseDto();

    }
}
