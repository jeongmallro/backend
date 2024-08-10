package com.example.joboasis.domain.member.service;

import com.example.joboasis.domain.member.dto.MemberRequestDto;
import com.example.joboasis.domain.member.dto.MemberResponseDto;
import com.example.joboasis.domain.member.enums.MemberJob;
import com.example.joboasis.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @AfterEach
    void after() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 일반 회원등록")
    void addMember() {

        //given
        MemberRequestDto memberDto = new MemberRequestDto("kim1234@joboasis.com", "password","Kim","Kim1234","01012345678", MemberJob.SOFTWARE_DEVELOPMENT);

        //when
        MemberResponseDto savedMemberDto = memberService.addMember(memberDto);

        //then
        assertThat(savedMemberDto.getEmail()).isEqualTo(memberDto.getEmail());
    }

    @Test
    @DisplayName("정상 이메일 중복 확인")
    void duplicatedEmail() {

        //given
        MemberRequestDto memberDtoA = new MemberRequestDto("kim1234@joboasis.com", "password","Kim","Kim1234","01012345678", MemberJob.SOFTWARE_DEVELOPMENT);
        MemberRequestDto memberDtoB = new MemberRequestDto("kim1234@joboasis.com", "password","Kim","Kim5678","01012345678", MemberJob.BUSINESS);

        //when
        memberService.addMember(memberDtoA);
        boolean checkEmail = memberService.checkEmail(memberDtoB.getEmail());

        //then
        assertThat(checkEmail).isTrue();
    }

    @Test
    @DisplayName("정상 아이디 중복 확인")
    void duplicatedId() {

        //given
        MemberRequestDto memberDtoA = new MemberRequestDto("kim1234@joboasis.com", "password","Kim","Kim1234","01012345678", MemberJob.SOFTWARE_DEVELOPMENT);
        MemberRequestDto memberDtoB = new MemberRequestDto("kim5678@joboasis.com", "password","Kim","Kim1234","01012345678", MemberJob.BUSINESS);

        //when
        memberService.addMember(memberDtoA);
        boolean checkId = memberService.checkId(memberDtoB.getLoginId());

        //then
        assertThat(checkId).isTrue();
    }

}
