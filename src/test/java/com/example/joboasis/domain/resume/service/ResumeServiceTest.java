package com.example.joboasis.domain.resume.service;

import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.member.repository.MemberRepository;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("정상 이력서 수정")
    void modifyResume() {

        //given
        ResumeRequestDto resumeDto = new ResumeRequestDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Member memberA = new Member( "Kim", "Kim1234");
        Member savedMember = memberRepository.save(memberA);
        Long memberId = savedMember.getId();

        String newTitle = "이력서 제목 1 수정";
        String newIntro = "열정 넘치는 3년차 개발자입니다.";
        ResumeRequestDto newResumeDto = new ResumeRequestDto(newTitle, newIntro, null, null, null);

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        ResumeResponseDto modifiedResumeDto = resumeService.modifyResume(savedResumeDto.getResumeId(), newResumeDto);

        //then
        assertThat(modifiedResumeDto.getTitle()).isEqualTo(newTitle);
        assertThat(modifiedResumeDto.getIntro()).isEqualTo(newIntro);
    }

    @Test
    @DisplayName("정상 이력서 조회")
    void findByResumeId() {

        //given
        ResumeRequestDto resumeDto = new ResumeRequestDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Member memberA = new Member( "Kim", "Kim1234");
        Member savedMember = memberRepository.save(memberA);
        Long memberId = savedMember.getId();

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        ResumeResponseDto resumeDtoFound = resumeService.getResume(savedResumeDto.getResumeId());

        //then
        assertThat(savedResumeDto.getResumeId()).isEqualTo(resumeDtoFound.getResumeId());
    }

    @Test
    @DisplayName("정상 이력서 리스트 조회")
    void findResumeList() {

        //given
        ResumeRequestDto resumeDto = new ResumeRequestDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        ResumeRequestDto resumeDto2 = new ResumeRequestDto("이력서 제목 2", "열정 넘치는 3년차 개발자입니다.", null, null, null);
        Member memberA = new Member( "Kim", "Kim1234");
        Member savedMember = memberRepository.save(memberA);
        Long memberId = savedMember.getId();

        //when
        resumeService.addResume(resumeDto, memberId);
        resumeService.addResume(resumeDto2, memberId);
        ArrayList<ResumeListDto> resumeList = resumeService.getResumes(memberId);

        //then
        assertThat(resumeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("정상 이력서 삭제")
    void deleteResume() {

        //given
        ResumeRequestDto resumeDto = new ResumeRequestDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Member memberA = new Member( "Kim", "Kim1234");
        Member savedMember = memberRepository.save(memberA);
        Long memberId = savedMember.getId();

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        Long savedResumeId = savedResumeDto.getResumeId();
        resumeService.removeResume(savedResumeId);

        //then
        assertThatThrownBy(() -> resumeService.getResume(savedResumeId)).isInstanceOf(IllegalArgumentException.class);
    }

}
