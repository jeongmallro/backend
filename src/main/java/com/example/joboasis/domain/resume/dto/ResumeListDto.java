package com.example.joboasis.domain.resume.dto;

import com.example.joboasis.domain.resume.enums.ResumeStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ResumeListDto {

    private String title;
    private ResumeStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}


