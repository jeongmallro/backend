package com.example.joboasis.domain.member.repository;

import com.example.joboasis.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}