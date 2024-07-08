package com.example.joboasis.domain.recruitment.entity;

import com.example.joboasis.common.entity.BaseEntity;
import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.joboasis.domain.recruitment.dto.RecruitmentResponseDto;
import com.example.joboasis.domain.recruitment.enums.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruitment_id")
	private Long id;
	private String title;
	private Integer recruitmentCount;
	private LocalDateTime closingDate;
	@Enumerated(EnumType.STRING)
	private RecruitmentStatus status;
	@CreationTimestamp
	private LocalDateTime postingDate;
	@UpdateTimestamp
	private LocalDateTime modifiedDate;
	private String detail;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_member_id")
	private CompanyMember companyMember;

	@Builder
	public Recruitment(
		String title,
		Integer recruitmentCount,
		LocalDateTime closingDate,
		String detail,
		CompanyMember companyMember
	) {
		this.title = title;
		this.recruitmentCount = recruitmentCount;
		this.closingDate = closingDate;
		this.detail = detail;
		this.companyMember = companyMember;
	}

	public RecruitmentResponseDto fromEntity() {
		return RecruitmentResponseDto.builder()
			.recruitmentId(this.id)
			.title(this.title)
			.recruitmentCount(this.recruitmentCount)
			.closingDate(this.closingDate)
			.status(this.status)
			.modifiedDate(this.modifiedDate)
			.postingDate(this.postingDate)
			.companyMemberId(this.companyMember.getId())
			.companyName(this.companyMember.getCompanyName())
			.build();
	}

	public Recruitment update(RecruitmentRequestDto request) {
		this.title = request.title();
		this.recruitmentCount = request.recruitmentCount();
		this.closingDate = request.closingDate();
		this.status = request.status();
		this.detail = request.detail();

		return this;
	}
}