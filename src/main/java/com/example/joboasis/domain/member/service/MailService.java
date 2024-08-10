package com.example.joboasis.domain.member.service;

import com.example.joboasis.domain.member.entity.EmailVerification;
import com.example.joboasis.domain.member.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;  //스프링 부트 사용 시 빈 자동 등록
    private final MailProperties mailProperties;
    private final EmailVerificationRepository emailVerificationRepository;

    public String createVerificationCode() {
        return String.valueOf((new Random().nextInt(900000) + 100000));
    }

    public void sendMail(String email) {  //중복 email 체크를 마친 email -> 현재 멤버 DB에 없는 email
        MimeMessage message = sender.createMimeMessage();

        try {
            Boolean exitsByEmail = emailVerificationRepository.existsByEmail(email);
            String verificationCode = saveVerification(email, exitsByEmail);

            message.setFrom(mailProperties.getUsername());
            message.setRecipients(MimeMessage.RecipientType.TO, email);

            String body = "";
            body += "<h1>" + verificationCode + "</h1>";
            message.setText(body, "UTF-8", "html");

            sender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }

    }

    public String saveVerification(String email, Boolean existsByEmail) {

        String verificationCode = createVerificationCode();

        if (existsByEmail.equals(true)) {  //이메일 있으면 -> 업데이트
            EmailVerification emailVerification = emailVerificationRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            emailVerification.updateEmailVerificationCode(verificationCode, LocalDateTime.now().plusMinutes(10));
        } else {  //이메일 없으면 -> 저장
            EmailVerification emailVerification = new EmailVerification(
                    email,
                    verificationCode,
                    LocalDateTime.now().plusMinutes(10)
            );

            emailVerificationRepository.save(emailVerification);
        }

        return verificationCode;
    }

    public boolean checkVerificationCode(String email, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);

        if (emailVerification.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        }

        return emailVerification.getVerificationCode().equals(verificationCode);
    }

}
