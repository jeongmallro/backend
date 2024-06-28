package com.example.joboasis.domain.recruitment.entity;

import com.example.joboasis.common.entity.BaseEntity;
import com.example.joboasis.domain.company.entity.CompanyMember;
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
		LocalDateTime closingDate,
		Long companyMemberId
	) {
		this.title = title;
		this.recruitmentCount = recruitmentCount;
		this.closingDate = closingDate;
	}
}