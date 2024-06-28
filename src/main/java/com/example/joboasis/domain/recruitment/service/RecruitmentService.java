package com.example.joboasis.domain.recruitment.service;

import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.company.repository.CompanyMemberRepository;
import com.example.joboasis.domain.recruitment.dto.RecruitmentDto;
import com.example.joboasis.domain.recruitment.entity.Recruitment;
import com.example.joboasis.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
	private final RecruitmentRepository recruitmentRepository;
	private final CompanyMemberRepository companyMemberRepository;

	@Transactional
	public void postingRecruitment(RecruitmentDto.Request request) {
		CompanyMember companyMember = companyMemberRepository.findById(request.companyMemberId())
			.orElseThrow(() -> new RuntimeException("기업회원 검증 오류"));

		Recruitment recruitment = request.toEntity();
		// TODO 공고 상태 변경하기.

		recruitmentRepository.save(recruitment);
	}
}