package com.example.joboasis.domain.company.controller;

import com.example.joboasis.domain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {
	private final CompanyService companyService;
}
