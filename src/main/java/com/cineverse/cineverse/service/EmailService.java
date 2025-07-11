package com.cineverse.cineverse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Async
    public void sendVerificationEmail(String toEmail, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email");
        message.setText("Click the link below to verify your email:\n" + link + "\n\nThis link will expire in 24 hours.");
        mailSender.send(message);
    }

    @Async
    public void sendResetPasswordEmail(String toEmail, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Password");
        message.setText("Click the link below to reset your password:\n" + link + "\n\nThis link will expire in 24 hours.");
        mailSender.send(message);
    }
}
