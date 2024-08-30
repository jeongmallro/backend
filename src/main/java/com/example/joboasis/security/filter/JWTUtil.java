package com.example.joboasis.security.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    @Value("${spring.jwt.access-token-expired-ms}")
    private Long accessTokenExpiredMs;

    @Value("${spring.jwt.refresh-token-expired-ms}")
    private Long refreshTokenExpiredMs;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getTokenType(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    public String getLoginId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId", String.class);
    }

    public String getAuthority(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("authority", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String tokenType, String loginId, String authority) {

        Long expiredMs = null;

        if (tokenType.equals("access")) {
            expiredMs = accessTokenExpiredMs;
        } else if (tokenType.equals("refresh")) {
            expiredMs = refreshTokenExpiredMs;
        }

        return Jwts.builder()
                .claim("tokenType", tokenType)  //access, refresh
                .claim("loginId", loginId)
                .claim("authority", authority)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))  //NullPointerException
                .signWith(secretKey)
                .compact();
    }


}