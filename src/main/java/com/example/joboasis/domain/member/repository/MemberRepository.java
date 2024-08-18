package com.example.joboasis.domain.member.repository;

import com.example.joboasis.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
}