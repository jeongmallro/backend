package com.example.joboasis.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class EmailVerificationCodeRepository {

    private final RedisTemplate<String, String> template;
    private final int LIMIT_TIME = 180;

    public void save(String email, String verificationCode) {
        template.opsForValue().set(email, verificationCode, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getVerificationCode(String email) {
        return template.opsForValue().get(email);
    }

}
