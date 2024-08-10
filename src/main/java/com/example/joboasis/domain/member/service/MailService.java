package com.example.joboasis.domain.member.service;

import com.example.joboasis.domain.member.repository.EmailVerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;  //스프링 부트 사용 시 빈 자동 등록
    private final MailProperties mailProperties;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    public String createVerificationCode() {
        return String.valueOf((new Random().nextInt(900000) + 100000));
    }

    public void sendMail(String email) {
        MimeMessage message = sender.createMimeMessage();

        try {
            String verificationCode = createVerificationCode();
            emailVerificationCodeRepository.save(email, verificationCode);

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

    public boolean checkVerificationCode(String email, String verificationCode) {
        String getVerificationCode = emailVerificationCodeRepository.getVerificationCode(email);
        return getVerificationCode.equals(verificationCode);
    }

}
