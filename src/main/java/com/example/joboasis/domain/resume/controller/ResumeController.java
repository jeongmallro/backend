package com.example.joboasis.domain.resume.controller;

import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeRequestDto;
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
    public ResponseEntity<ResumeResponseDto> addResume(@RequestBody ResumeRequestDto resumeDto,
                                                       @RequestHeader Long token) {  //@CookieValue String
        Long memberId = resumeService.getMember(token);
        ResumeResponseDto newResumeDto = resumeService.addResume(resumeDto, memberId);
        return new ResponseEntity<>(newResumeDto, HttpStatus.CREATED);
    }

    @PutMapping("/{resume_id}")
    public ResumeResponseDto modifyResume(@RequestHeader Long token,  //@CookieValue String
                                          @PathVariable(name = "resume_id") Long resumeId,
                                          @RequestBody ResumeRequestDto resumeDto) {
        resumeService.getMember(token);
        return resumeService.modifyResume(resumeId, resumeDto);
    }

    @GetMapping("/{resume_id}")
    public ResumeResponseDto getResume(@PathVariable(name = "resume_id") Long resumeId,
                                       @RequestHeader Long token) {  //@CookieValue String
        resumeService.getMember(token);
        return resumeService.getResume(resumeId);
    }

    @GetMapping
    public ArrayList<ResumeListDto> getResumes(@RequestHeader Long token) {  //@CookieValue String
        Long memberId = resumeService.getMember(token);
        return resumeService.getResumes(memberId);
    }

    @DeleteMapping("/{resume_id}")
    public void removeResume(@PathVariable(name = "resume_id") Long resumeId,
                             @RequestHeader Long token) {  //@CookieValue String
        resumeService.getMember(token);
        resumeService.removeResume(resumeId);
    }

}
