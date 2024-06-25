package com.example.joboasis.domain.resume.controller;

import com.example.joboasis.domain.resume.dto.ResumeDto;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;
import com.example.joboasis.domain.resume.service.ResumeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeResponseDto> createResume(@RequestBody ResumeDto resumeDto,
                                                          @SessionAttribute Long memberId) {
        ResumeResponseDto newResumeDto = resumeService.addResume(resumeDto, memberId);
        return new ResponseEntity<>(newResumeDto, HttpStatus.CREATED);
    }

    @GetMapping("/{resume_id}")
    public ResumeResponseDto getResume(@PathVariable Long resumeId,
                                       @SessionAttribute Long memberId) {
        return resumeService.findResume(resumeId);
    }

    @GetMapping
    public ArrayList<ResumeListDto> getResumes(@SessionAttribute Long memberId) {
        return resumeService.findResumeList(memberId);
    }

    @PatchMapping("/{resume_id}")
    public ResumeResponseDto modifyResume(@SessionAttribute Long memberId,
                                          @PathVariable Long resumeId,
                                          @RequestBody ResumeDto resumeDto) {

        return resumeService.modifyResume(resumeId, resumeDto);
    }

    @DeleteMapping("/{resume_id}")
    public void deleteResume(@PathVariable Long resumeId,
                             @SessionAttribute Long memberId) {
        resumeService.deleteResume(resumeId);
    }

}
