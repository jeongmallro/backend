package com.example.joboasis.domain.company.entity;

import jakarta.persistence.*;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id")
	private Long id;
}
