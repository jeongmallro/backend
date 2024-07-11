package com.example.joboasis.domain.recruitment.controller;

import com.example.joboasis.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.joboasis.domain.recruitment.dto.RecruitmentResponseDto;
import com.example.joboasis.domain.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecruitmentController {
	private final RecruitmentService recruitmentService;

	@PostMapping("/recruitments")
	public void postingRecruitment(@RequestBody RecruitmentRequestDto request) {
		recruitmentService.postingRecruitment(request);
	}

	@PutMapping("/recruitments/{id}")
	public RecruitmentResponseDto modifyRecruitment(@PathVariable(name = "id") Long recruitmentId,
													@RequestBody RecruitmentRequestDto request) {
		return recruitmentService.modifyRecruitment(recruitmentId, request);
	}

	@DeleteMapping("/recruitments/{id}")
	public void deleteRecruitment(@PathVariable(name = "id") Long recruitmentId,
								  @RequestBody RecruitmentRequestDto request) {
		recruitmentService.deleteRecruitment(recruitmentId, request);
	}

	@GetMapping("/recruitments")
	public List<RecruitmentResponseDto> getRecruitmentList() {
		return recruitmentService.getRecruitmentList();
	}
}