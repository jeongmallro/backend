package com.example.joboasis.domain.member.repository;

import com.example.joboasis.domain.member.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {

    Boolean existsByEmail(String email);
    Optional<EmailVerification> findByEmail(String email);
}
