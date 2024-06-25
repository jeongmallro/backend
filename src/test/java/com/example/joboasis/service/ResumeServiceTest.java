package com.example.joboasis.service;

import com.example.joboasis.domain.resume.dto.ResumeDto;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
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
    ResumeRepository resumeRepository;
    @Autowired
    ResumeService resumeService;

    @AfterEach
    void clear() {
        resumeRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 이력서 등록")
    void addResume() {

        //given
        ResumeDto resumeDto = new ResumeDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Long memberId = 1L;

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);

        //then
        assertThat(resumeDto.getTitle()).isEqualTo(savedResumeDto.getTitle());

    }

    @Test
    @DisplayName("정상 이력서 조회")
    void findByResumeId() {

        //given
        ResumeDto resumeDto = new ResumeDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Long memberId = 1L;

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        ResumeResponseDto resumeDtoFound = resumeService.findResume(savedResumeDto.getResumeId());

        //then
        assertThat(savedResumeDto.getResumeId()).isEqualTo(resumeDtoFound.getResumeId());

    }

    @Test
    @DisplayName("정상 이력서 리스트 조회")
    void findResumeList() {

        //given
        ResumeDto resumeDto = new ResumeDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        ResumeDto resumeDto2 = new ResumeDto("이력서 제목 2", "열정 넘치는 3년차 개발자입니다.", null, null, null);
        Long memberId = 1L;

        //when
        resumeService.addResume(resumeDto, memberId);
        resumeService.addResume(resumeDto2, memberId);
        ArrayList<ResumeListDto> resumeList = resumeService.findResumeList(memberId);

        //then
        assertThat(resumeList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("정상 이력서 수정")
    void modifyResume() {

        //given
        ResumeDto resumeDto = new ResumeDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Long memberId = 1L;

        String newTitle = "이력서 제목 1 수정";
        String newIntro = "열정 넘치는 3년차 개발자입니다.";
        ResumeDto newResumeDto = new ResumeDto(newTitle, newIntro, null, null, null);

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        ResumeResponseDto modifiedResumeDto = resumeService.modifyResume(savedResumeDto.getResumeId(), newResumeDto);

        //then
        assertThat(modifiedResumeDto.getTitle()).isEqualTo(newTitle);
        assertThat(modifiedResumeDto.getIntro()).isEqualTo(newIntro);
    }

    @Test
    @DisplayName("정상 이력서 삭제")
    void deleteResume() {

        //given
        ResumeDto resumeDto = new ResumeDto("이력서 제목 1", "꼼꼼한 3년차 개발자입니다.", null, null, null);
        Long memberId = 1L;

        //when
        ResumeResponseDto savedResumeDto = resumeService.addResume(resumeDto, memberId);
        Long savedResumeId = savedResumeDto.getResumeId();
        resumeService.deleteResume(savedResumeId);

        //then
        assertThatThrownBy(() -> resumeService.findResume(savedResumeId)).isInstanceOf(IllegalArgumentException.class);
    }

}
