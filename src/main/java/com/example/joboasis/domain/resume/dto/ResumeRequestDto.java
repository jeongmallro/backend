package com.example.joboasis.domain.resume.dto;

import com.example.joboasis.domain.resume.entity.Resume;
import com.example.joboasis.domain.resume.enums.ResumeStatus;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRequestDto {

    private String title;
    private String intro;
    private List<String> careerList;
    private List<String> educationList;
    private List<String> skill;

    public Resume toEntity() {
        return Resume.builder()
                .title(title)
                .intro(intro)
                .careerList(careerList)
                .educationList(educationList)
                .skill(skill)
                .status(ResumeStatus.IN_PROGRESS)
                .build();
    }

}
