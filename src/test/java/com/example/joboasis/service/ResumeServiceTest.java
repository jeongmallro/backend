package com.example.joboasis.service;

import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.member.repository.MemberRepository;
import com.example.joboasis.domain.resume.dto.ResumeRequestDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;
import com.example.joboasis.domain.resume.repository.ResumeRepository;
import com.example.joboasis.domain.resume.service.ResumeService;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ResumeServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ResumeRepository resumeRepository;
    @Autowired
    ResumeService resumeService;

    @AfterEach
    void after() {
        resumeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 이력서 등록")
    void addResume() {

        //given
        ResumeRequestDto resumeDto = new ResumeRequestDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Member memberA = new Member("Kim","Kim1234");
        Member savedMember = memberRepository.save(memberA);
        Long memberId = savedMember.getId();

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);

        //then
        assertThat(resumeDto.getTitle()).isEqualTo(savedResumeDto.getTitle());

    }

}
