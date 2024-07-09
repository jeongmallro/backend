package com.example.joboasis.domain.recruitment.service;

import com.example.joboasis.domain.company.entity.CompanyMember;
import com.example.joboasis.domain.company.repository.CompanyMemberRepository;
import com.example.joboasis.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.joboasis.domain.recruitment.dto.RecruitmentResponseDto;
import com.example.joboasis.domain.recruitment.entity.Recruitment;
import com.example.joboasis.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
	private final RecruitmentRepository recruitmentRepository;
	private final CompanyMemberRepository companyMemberRepository;

	@Transactional
	public void postingRecruitment(RecruitmentRequestDto request) {
		CompanyMember companyMember = companyMemberRepository.findById(request.companyMemberId())
			.orElseThrow(() -> new RuntimeException("기업회원 검증 오류"));

		Recruitment recruitment = request.toEntity(companyMember);
		// TODO 공고 상태 변경하기.

		recruitmentRepository.save(recruitment);
	}

	@Transactional
	public RecruitmentResponseDto modifyRecruitment(Long recruitmentId, RecruitmentRequestDto request) {
		Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 공고입니다."));

		if (!Objects.equals(recruitment.getCompanyMember().getId(), request.companyMemberId())) {
			throw new RuntimeException("해당 공고를 수정할 권한이 없습니다.");
		}

		return recruitment.update(request).fromEntity();
	}

	@Transactional(readOnly = true)
	public List<RecruitmentResponseDto> getRecruitmentList() {
		// TODO 공고 상태에 따른 조회로 변경
		List<Recruitment> recruitments = recruitmentRepository.findAll();

		return recruitments.stream().map(Recruitment::fromEntity).toList();
	}
}