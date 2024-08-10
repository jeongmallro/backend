package com.example.joboasis.domain.member.dto;

import com.example.joboasis.domain.member.enums.MemberJob;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {

    private Long id;
    private String name;
    private String loginId;
    private String email;
    private String phoneNumber;
    private MemberJob job;
}
