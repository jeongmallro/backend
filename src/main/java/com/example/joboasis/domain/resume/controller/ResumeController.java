package com.example.joboasis.domain.resume.controller;

import com.example.joboasis.domain.resume.dto.ResumeRequestDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;
import com.example.joboasis.domain.resume.service.ResumeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeResponseDto> addResume(@RequestBody ResumeRequestDto resumeDto,
                                                       @RequestHeader Long token) {  //@CookieValue String
        Long memberId = resumeService.getMember(token);
        ResumeResponseDto newResumeDto = resumeService.addResume(resumeDto, memberId);
        return new ResponseEntity<>(newResumeDto, HttpStatus.CREATED);
    }
}
