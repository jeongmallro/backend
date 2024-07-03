package com.example.joboasis.domain.recruitment.dto;

import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.recruitment.entity.Recruitment;
import com.example.joboasis.domain.recruitment.enums.RecruitmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class RecruitmentDto {
	public record Request(
		Long companyMemberId,
		String title,
		Integer recruitmentCount,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime closingDate,
		RecruitmentStatus status,
		String detail
	) {
		public Recruitment toEntity() {
			return Recruitment.builder()
					.title(title)
					.recruitmentCount(recruitmentCount)
					.closingDate(closingDate)
					.build();
		}
	}

	@Getter
	@Builder
	public static class Response {
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
}