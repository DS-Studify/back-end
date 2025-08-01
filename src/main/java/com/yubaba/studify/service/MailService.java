package com.yubaba.studify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final RedisService redisService;

    @Async
    public void sendAuthCode(String toEmail) {
        String code = createAuthCode();
        redisService.setEmailAuthCode(toEmail, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Studify 이메일 인증 코드");
        message.setText("인증 코드: " + code + "\n5분 내에 입력해주세요.");

        mailSender.send(message);
    }

    @Async
    public void resendAuthCode(String toEmail) {
        String newCode = createAuthCode();
        redisService.replaceEmailAuthCode(toEmail, newCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Studify 이메일 인증 코드");
        message.setText("인증 코드: " + newCode + "\n5분 내에 입력해주세요.");

        mailSender.send(message);
    }

    private String createAuthCode() {
        return String.format("%06d", new Random().nextInt(999999)); // 6자리 숫자
    }
}
