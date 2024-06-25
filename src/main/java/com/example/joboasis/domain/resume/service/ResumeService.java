package com.example.joboasis.domain.resume.service;

import com.example.joboasis.domain.resume.dto.ResumeDto;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;
import com.example.joboasis.domain.resume.entity.Resume;
import com.example.joboasis.domain.resume.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.DynamicUpdate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@DynamicUpdate
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    @Transactional
    public ResumeResponseDto addResume(ResumeDto resumeDto, Long memberId) {
        Resume resume = resumeDto.toEntity();
        resume.setMemberId(memberId);
        Resume savedResume = resumeRepository.save(resume);
        log.info("resumeId={}", savedResume.getResumeId());

        return savedResume.toResponseDto();
    }

    public ResumeResponseDto findResume(Long resumeId) {
        Resume resume = resumeRepository.findByResumeId(resumeId).orElseThrow(IllegalArgumentException::new);  //에러 생성하기
        return resume.toResponseDto();
    }

    public ArrayList<ResumeListDto> findResumeList(Long memberId) {
        List<Resume> resumes = resumeRepository.findAllByMemberId(memberId);  //null 처리
        ArrayList<ResumeListDto> resumeDtoList = new ArrayList<>();

        for (Resume resume : resumes) {
            resumeDtoList.add(resume.toListDto());
        }

        return resumeDtoList;

    }

    @Transactional
    public ResumeResponseDto modifyResume(Long resumeId, ResumeDto resumeDto) {
        Resume resume = resumeRepository.findByResumeId(resumeId).orElseThrow(IllegalArgumentException::new);

        resume.setTitle(resumeDto.getTitle());
        resume.setIntro(resumeDto.getIntro());
        resume.setCareerList(resumeDto.getCareerList());
        resume.setEducationList(resumeDto.getEducationList());
        resume.setSkill(resumeDto.getSkill());

        return resume.toResponseDto();
    }

    @Transactional
    public void deleteResume(Long resumeId) {
        Resume findResume = resumeRepository.findByResumeId(resumeId).orElseThrow(IllegalArgumentException::new);
        resumeRepository.delete(findResume);
    }

}
