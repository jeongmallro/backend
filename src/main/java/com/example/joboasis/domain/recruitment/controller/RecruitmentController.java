package com.example.joboasis.domain.recruitment.controller;

import com.example.joboasis.domain.recruitment.dto.RecruitmentDto;
import com.example.joboasis.domain.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitmentController {
	private final RecruitmentService recruitmentService;

	@PostMapping("/recruitments")
	public void postingRecruitment(@RequestBody RecruitmentDto.Request request) {
		recruitmentService.postingRecruitment(request);
	}

	@PostMapping("/recruitments/{id}")
	public RecruitmentDto.Response modifyRecruitment(@PathVariable(name = "id") Long recruitmentId,
													 @RequestBody RecruitmentDto.Request request) {
		return recruitmentService.modifyRecruitment(recruitmentId, request);
	}
}