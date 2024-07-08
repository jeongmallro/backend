package com.example.joboasis.domain.resume.entity;

import com.example.joboasis.common.entity.BaseEntity;
import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.resume.dto.ResumeListDto;
import com.example.joboasis.domain.resume.dto.ResumeRequestDto;
import com.example.joboasis.domain.resume.dto.ResumeResponseDto;

import com.example.joboasis.domain.resume.enums.ResumeStatus;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    private String title;

    private String intro;

    private ResumeStatus status;

    @Column(name = "career_list")
    private List<String> careerList;

    @Column(name = "education_list")
    private List<String> educationList;

    private List<String> skill;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Resume(String title, String intro, ResumeStatus status, List<String> careerList, List<String> educationList, List<String> skill) {
        this.title = title;
        this.intro = intro;
        this.status = status;
        this.careerList = careerList;
        this.educationList = educationList;
        this.skill = skill;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void editResume(ResumeRequestDto resumeDto) {
        this.title = resumeDto.getTitle();
        this.intro = resumeDto.getIntro();
        this.careerList = resumeDto.getCareerList();
        this.educationList = resumeDto.getEducationList();
        this.skill = resumeDto.getSkill();
    }

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
