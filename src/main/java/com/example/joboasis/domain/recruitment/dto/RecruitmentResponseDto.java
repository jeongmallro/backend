package com.example.joboasis.domain.recruitment.dto;

import com.example.joboasis.domain.recruitment.enums.RecruitmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecruitmentResponseDto {
	private Long recruitmentId;
	private String title;
	private Integer recruitmentCount;
	private LocalDateTime closingDate;
	private RecruitmentStatus status;
	private LocalDateTime postingDate;
	private LocalDateTime modifiedDate;
	private String detail;
	private Long companyMemberId;
	private String companyName;
}