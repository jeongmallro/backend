package com.example.joboasis.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SigninFilter extends AbstractAuthenticationProcessingFilter {  //JSON 으로 loginId, password 받는 CustomAbstractAuthenticationProcessingFilter 구현

    public static final String USERNAME_KEY = "loginId";

    public static final String PASSWORD_KEY = "password";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/signin", "POST");

    private boolean postOnly = true;

    private final JWTUtil jwtUtil;

    private final ObjectMapper objectMapper;


    public SigninFilter(JWTUtil jwtUtil, ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {  //IOException 처리

        //Http Method 에러 핸들링
        if (this.postOnly && !request.getMethod().equals("POST")) {
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
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(loginId, password);  //since 5.7

        Object principal = authenticationToken.getPrincipal();
        /**
         * AuthenticationManager 이 authentication 을 수행하도록 UsernamePasswordAuthenticationToken 패스
         * AuthenticationManager 의 구현체 AuthenticationProvider 종류 중 DaoAuthenticationProvider 가 UserDetailService 를 통해 UserDetails 찾아온다. (loadUserByUsername)
         * PasswordEncoder 를 사용해서 UserDetails 의 password 를 입증한다.
         * authentication 이 반환되고 결과에 따라 아래의 메서드 중 하나 수행 (AbstractAuthenticationProcessingFilter 소스코드 참조)
         */

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * authentication 성공 시, jwt 토큰 생성해서 응답
     * 아래와 같이 커스텀했기 때문에 authentication 성공 시에 DaoAuthenticationProvider 가 수행하기로 했던 행동은 사라짐
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //authentication 에서 loginId 값 추출
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String loginId = customUserDetails.getUsername();

        //authentication 에서 authority 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String authority = auth.getAuthority();

        //jwt 토큰 생성
        String token = jwtUtil.createJwt(loginId, authority, 60 * 10 * 10 * 10 * 10L);  //10분

        response.addHeader("Authorization", "Bearer " + token);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}