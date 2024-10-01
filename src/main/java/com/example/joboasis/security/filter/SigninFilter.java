package com.example.joboasis.security.filter;

import com.example.joboasis.security.token.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SigninFilter extends AbstractAuthenticationProcessingFilter {  //JSON 으로 loginId, password 값 받아서 JWT Token 반환

    public static final String USERNAME_KEY = "loginId";
    public static final String PASSWORD_KEY = "password";
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;
    @Value("${spring.jwt.refresh-token-expired-sec}")
    private int refreshTokenExpiredSec;


    public SigninFilter(JWTUtil jwtUtil, ObjectMapper objectMapper, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        super(new OrRequestMatcher(new AntPathRequestMatcher("/signin", "POST"),
                new AntPathRequestMatcher("/company/signin", "POST")), authenticationManager);
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.refreshTokenService = refreshTokenService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {  //IOException 처리

        //Http Method 에러 핸들링
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        //Content-Type 에러 핸들링

        //MessageBody 에서 loginId, password 값 추출
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        Map<String, String> loginIdPasswordMap = objectMapper.readValue(messageBody, Map.class);

        String loginId = loginIdPasswordMap.get(USERNAME_KEY);
        String password = loginIdPasswordMap.get(PASSWORD_KEY);

        //MessageBody 로 부터 추출한 loginId, password 값을 가지고 UsernamePasswordAuthenticationToken 생성
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginId, password);

        return this.getAuthenticationManager().authenticate(authentication);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //authentication 에서 loginId 값 추출
        String loginId = authentication.getName();

        //authentication 에서 authority 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String authority = grantedAuthority.getAuthority();

        //토큰 생성
        String accessToken = jwtUtil.createJwt("access", loginId, authority);
        String refreshToken = jwtUtil.createJwt("refresh", loginId, authority);

        //Refresh 토큰 DB에 저장
        refreshTokenService.addRefreshToken(loginId, refreshToken);

        //응답 설정
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);  //쿠키 방식으로 전송시에 "Bearer " 이후 띄어쓰기가 들어가면 쿠키에서 오류가 발생
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(refreshTokenExpiredSec);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);

        return cookie;
    }
}
