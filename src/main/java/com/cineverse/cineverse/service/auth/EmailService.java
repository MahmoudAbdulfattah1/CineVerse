package com.cineverse.cineverse.service.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendVerificationEmail(String toEmail, String username, String verificationLink) {
        String html = loadTemplate("classpath:templates/verify-email.html");
        html = html.replace("{{username}}", username)
                .replace("{{verificationLink}}", verificationLink);
        sendHtmlEmail(toEmail, "Verify Your Email", html);
    }

    @Async
    public void sendResetPasswordEmail(String toEmail, String username, String resetLink) {
        String html = loadTemplate("classpath:templates/reset-password.html");
        html = html.replace("{{username}}", username)
                .replace("{{resetLink}}", resetLink);
        sendHtmlEmail(toEmail, "Reset Your Password", html);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(senderEmail, "🎥 CineVerse Support");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: ", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadTemplate(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template: " + path, e);
        }
    }
}

