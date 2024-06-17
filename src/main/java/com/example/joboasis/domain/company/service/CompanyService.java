package com.example.joboasis.domain.company.service;

import com.example.joboasis.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
	private final CompanyRepository companyRepository;
}
