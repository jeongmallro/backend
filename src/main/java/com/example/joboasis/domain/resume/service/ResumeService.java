package com.example.joboasis.domain.resume.service;

import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.member.repository.MemberRepository;
import com.example.joboasis.domain.resume.dto.ResumeRequestDto;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;
import com.example.joboasis.domain.resume.entity.Resume;
import com.example.joboasis.domain.resume.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.DynamicUpdate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@DynamicUpdate
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Long getMember(Long token) {  //0. String token 으로 바꾸기
        //1. token 복호화
        //2. 1번으로부터 사용자 아이디(혹은 사용자 이메일 겟)
        Long memberId = token;
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        return member.getId();
        //3. 예외 처리

        /*
        - 이력서 조회, 수정, 삭제
        : 해당 사용자가 존재하는지 확인까지만 하면 됨(getMember)

        - 생성
        : 해당 사용자가 존재하는지 확인하고(getMember), Resume 객체에 setMember 해줘야 함

        - 이력서 리스트 조회
        : 해당 사용자가 존재하는지 확인하고(getMember), MemberId 로 디비에서 이력서들 겟 해와야 함
         */
    }


    @Transactional
    public ResumeResponseDto addResume(ResumeRequestDto resumeDto, Long memberId) {
        Resume resume = resumeDto.toEntity();
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);  //코드 중복
        resume.setMember(member);
        Resume savedResume = resumeRepository.save(resume);
        log.info("resumeId={}", savedResume.getResumeId());

        return savedResume.toResponseDto();
    }

}
