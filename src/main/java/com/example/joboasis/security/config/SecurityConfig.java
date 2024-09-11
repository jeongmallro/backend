package com.example.joboasis.security.config;

import com.example.joboasis.security.filter.SignoutFilter;
import com.example.joboasis.security.filter.JWTFilter;
import com.example.joboasis.security.filter.JWTUtil;
import com.example.joboasis.security.filter.SigninFilter;
import com.example.joboasis.security.token.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .httpBasic((authorize) -> authorize.disable())
                .formLogin((authorize) -> authorize.disable())
                .logout((logout) -> logout.disable())

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/company/signout", "/signout", "/signup/**", "/", "/company/signup").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/profile", "/resumes/**", "/applications/**").hasRole("MEMBER")  //일반회원
                        .requestMatchers(regexMatcher("/recruitments/[0-9]+/applications")).hasRole("MEMBER")
                        .requestMatchers("/recruitments/**").hasRole("COMPANY")  //기업회원
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  //Swagger
                        .anyRequest().denyAll())

                .addFilterAt(new SigninFilter(jwtUtil, objectMapper, authenticationManager(), refreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), SigninFilter.class)
                .addFilterAt(new SignoutFilter(jwtUtil, refreshTokenService), LogoutFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();

    }
}

