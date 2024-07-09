package com.example.joboasis.domain.resume.dto;

import com.example.joboasis.domain.member.entity.Member;
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

    public Resume toEntity(Member member) {
        return Resume.builder()
                .title(title)
                .intro(intro)
                .careerList(careerList)
                .educationList(educationList)
                .skill(skill)
                .status(ResumeStatus.IN_PROGRESS)
                .member(member)
                .build();
    }

}
