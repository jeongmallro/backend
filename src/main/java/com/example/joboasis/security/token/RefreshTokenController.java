package com.example.joboasis.security.token;

import com.example.joboasis.security.filter.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    @Value("${spring.jwt.refresh-token-expired-sec}")
    private int refreshTokenExpiredSec;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueTokens(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
            //로그아웃
        }

        // 토큰이 refresh 인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getTokenType(refreshToken);

        if (!tokenType.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenService.existsByRefreshToken(refreshToken);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String loginId = jwtUtil.getLoginId(refreshToken);
        String authority = jwtUtil.getAuthority(refreshToken);

        //make new JWT
        String newAccessToken = jwtUtil.createJwt("access", loginId, authority);
        String newRefreshToken = jwtUtil.createJwt("refresh", loginId, authority);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenService.deleteByRefreshToken(refreshToken);
        refreshTokenService.addRefreshToken(loginId, newRefreshToken);

        //response
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
        response.addCookie(createCookie("refreshToken", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(refreshTokenExpiredSec);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);

        return cookie;
    }

}
