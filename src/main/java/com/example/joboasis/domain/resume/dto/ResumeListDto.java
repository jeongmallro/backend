package com.example.joboasis.domain.resume.dto;

import com.example.joboasis.domain.resume.entity.ResumeStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ResumeListDto {

    private String title;
    private ResumeStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
