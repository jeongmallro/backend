package com.example.joboasis.domain.resume.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class ResumeResponseDto {

    private Long resumeId;
    private String title;
    private String intro;
    private List<String> careerList;
    private List<String> educationList;
    private List<String> skill;

}
