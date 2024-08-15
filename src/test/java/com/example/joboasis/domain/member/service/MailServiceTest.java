package com.example.joboasis.domain.member.service;

import com.example.joboasis.domain.member.repository.EmailVerificationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailServiceTest {

    @Autowired
    MailService mailService;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;

    @AfterEach
    void after() {
        emailVerificationRepository.deleteAll();
    }

    @Test
    @DisplayName("정상 이메일 인증 저장")
    void saveVerification() {

        //given
        String email = "kim1234@joboasis.com";
        Boolean existsByEmail = emailVerificationRepository.existsByEmail(email);

        //when
        String verificationCode = mailService.saveVerification(email, existsByEmail);

        //then
        boolean checkVerificationCode = mailService.checkVerificationCode(email, verificationCode);
        Assertions.assertThat(checkVerificationCode).isTrue();
    }

    @Test
    @DisplayName("정상 이메일 인증 갱신")
    void updateVerification() {

        //given
        String email = "kim1234@joboasis.com";
        Boolean beforeExistsByEmail = emailVerificationRepository.existsByEmail(email);
        String beforeVerificationCode = mailService.saveVerification(email, beforeExistsByEmail);

        //when
        Boolean afterExistsByEmail = emailVerificationRepository.existsByEmail(email);
        String afterVerificationCode = mailService.saveVerification(email, afterExistsByEmail);

        //then
        Assertions.assertThat(beforeVerificationCode).isNotEqualTo(afterVerificationCode);

    }


}
