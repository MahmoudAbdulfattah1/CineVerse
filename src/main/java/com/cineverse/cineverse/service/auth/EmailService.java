package com.cineverse.cineverse.service.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
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

    /**
     * Sends an asynchronous verification email to a user with a clickable link to verify their account.
     *
     * @param toEmail          the recipient's email address
     * @param username         the username of the recipient (used for personalization)
     * @param verificationLink the verification link to be included in the email
     */
    @Async
    public void sendVerificationEmail(String toEmail, String username, String verificationLink) {
        String html = loadTemplate("classpath:templates/verify-email.html");
        html = html.replace("{{username}}", username)
                .replace("{{verificationLink}}", verificationLink);
        sendHtmlEmail(toEmail, "Verify Your Email", html);
    }

    /**
     * Sends an asynchronous password reset email to a user with a link to reset their password.
     *
     * @param toEmail   the recipient's email address
     * @param username  the username of the recipient (used for personalization)
     * @param resetLink the password reset link to be included in the email
     */
    @Async
    public void sendResetPasswordEmail(String toEmail, String username, String resetLink) {
        String html = loadTemplate("classpath:templates/reset-password.html");
        html = html.replace("{{username}}", username)
                .replace("{{resetLink}}", resetLink);
        sendHtmlEmail(toEmail, "Reset Your Password", html);
    }

    /**
     * Sends an HTML-formatted email to the specified recipient.
     *
     * @param to          the recipient's email address
     * @param subject     the subject of the email
     * @param htmlContent the HTML body of the email
     * @throws RuntimeException if the email fails to send
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(senderEmail, MimeUtility.encodeText("🎥 CineVerse Support", "UTF-8", "B"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email: ", e);
        }
    }

    /**
     * Loads an email template file from the specified path.
     *
     * @param path the path to the template file (supports Spring resource paths such as classpath:)
     * @return the contents of the template file as a String
     * @throws RuntimeException if the template cannot be loaded
     */
    private String loadTemplate(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template: " + path, e);
        }
    }
}

