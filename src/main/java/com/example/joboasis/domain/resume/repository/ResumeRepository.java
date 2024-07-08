package com.example.joboasis.domain.resume.repository;

import com.example.joboasis.domain.member.entity.Member;
import com.example.joboasis.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByResumeId(Long resumeId);
    List<Resume> findAllByMember(Member member);

}
