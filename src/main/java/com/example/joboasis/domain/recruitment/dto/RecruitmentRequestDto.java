package com.example.joboasis.domain.recruitment.dto;

import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.recruitment.entity.Recruitment;
import com.example.joboasis.domain.recruitment.enums.RecruitmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RecruitmentRequestDto(
	Long companyMemberId,
	String title,
	Integer recruitmentCount,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	LocalDateTime closingDate,
	RecruitmentStatus status,
	String detail
) {
	public Recruitment toEntity(CompanyMember companyMember) {
		return Recruitment.builder()
			.title(title)
			.recruitmentCount(recruitmentCount)
			.closingDate(closingDate)
			.companyMember(companyMember)
			.build();
	}
}