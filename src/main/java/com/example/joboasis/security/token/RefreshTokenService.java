package com.example.joboasis.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.refresh-token-expired-ms}")
    private Long expiredMs;

    @Transactional
    public void addRefreshToken(String loginId, String token) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshToken = new RefreshToken(loginId, token, date.toString());

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteByRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Transactional(readOnly = true)
    public boolean existsByRefreshToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }
}
