package com.example.joboasis.domain.recruitment.entity;

import com.example.joboasis.common.entity.BaseEntity;
import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.recruitment.dto.RecruitmentDto;
import com.example.joboasis.domain.recruitment.enums.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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
		LocalDateTime closingDate
	) {
		this.title = title;
		this.recruitmentCount = recruitmentCount;
		this.closingDate = closingDate;
	}

	public RecruitmentDto.Response toDto() {
		return RecruitmentDto.Response.builder()
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

	public Recruitment update(RecruitmentDto.Request request) {
		this.title = request.title();
		this.recruitmentCount = request.recruitmentCount();
		this.closingDate = request.closingDate();
		this.status = request.status();

		return this;
	}
}