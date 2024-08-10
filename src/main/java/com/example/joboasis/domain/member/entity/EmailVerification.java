package com.example.joboasis.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    private String email;
    private String verificationCode;
    private LocalDateTime expireDate;

    public void updateEmailVerificationCode(String verificationCode, LocalDateTime expireDate) {
        this.verificationCode = verificationCode;
        this.expireDate = expireDate;
    }
}
