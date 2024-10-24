package com.example.joboasis.security.filter;

import com.example.joboasis.domain.member.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);  //Header 로부터 access token 얻기

        if (authorization == null || !authorization.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];  //Bearer 제거

        try {  //access token 만료 확인
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {  //JWTUtil 에서 파싱 과정에서 발생하는 예외를 핸들링하면 여기서 if 분기로 처리 가능

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;  //프론트에서 /reissue 로 redirect
        }

        String tokenType = jwtUtil.getTokenType(accessToken);

        if (!tokenType.equals("access")) {  //access token 이 맞는지 확인

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String loginId = jwtUtil.getLoginId(accessToken);
        String authority = jwtUtil.getAuthority(accessToken);

        Member member = new Member(loginId, authority);

        //UserDetails 에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {  //reissue 는 JWTFilter 를 거치지 않게 처리
        return request.getRequestURI().equals("/reissue");
    }
}