package com.example.joboasis.domain.company.entity;

import com.example.joboasis.domain.recruitment.entity.Recruitment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_member_id")
	private Long id;
	private String email;
	private String password;
	private String companyName;
	private String field;
	private Integer employeesNumber;
	private String location;
	private String info;

	@OneToOne(mappedBy = "companyMember", fetch = FetchType.LAZY)
	private Recruitment recruitment;

	@Builder
	public CompanyMember(
		String email,
		String password,
		String companyName,
		String field,
		Integer employeesNumber,
		String location,
		String info
	) {
		this.email = email;
		this.password = password;
		this.companyName = companyName;
		this.field = field;
		this.employeesNumber = employeesNumber;
		this.location = location;
		this.info = info;
	}
}