package com.example.joboasis.domain.resume.entity;

import com.example.joboasis.common.entity.BaseEntity;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    @Column(name = "title")
    private String title;

    @Column(name = "intro")
    private String intro;

    @Column(name = "status")
    private ResumeStatus status;

    @Column(name = "career_list")
    private List<String> careerList;

    @Column(name = "education_list")
    private List<String> educationList;

    @Column(name = "skill")
    private List<String> skill;

    @Column(name = "member_id")
    private Long memberId;

/*
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
*/

    public ResumeResponseDto toResponseDto() {
        return ResumeResponseDto.builder()
                .resumeId(resumeId)
                .title(title)
                .intro(intro)
                .careerList(careerList)
                .educationList(educationList)
                .skill(skill)
                .build();
    }

    public ResumeListDto toListDto() {
        return ResumeListDto.builder()
                .title(title)
                .status(status)
                .createdDate(getCreatedDate())
                .modifiedDate(getModifiedDate())
                .build();
    }

}
