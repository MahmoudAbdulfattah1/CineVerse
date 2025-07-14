package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    @Value("${app.frontend.url}")
    private String frontendUrl;


    public void register(User user) {
        User savedUser = userService.registerUser(user);
        String token = jwtService.generateToken(new UserPrincipal(savedUser));
        String link = frontendUrl + "auth/verify?token=" + token;
        emailService.sendVerificationEmail(savedUser.getEmail(), user.getUsername(), link);
    }


    public User login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }


    public String forgetPasswordToken(User user) {
        String jwtToken = jwtService.generateToken(new UserPrincipal(user));
        String link = frontendUrl + "auth/reset-password?token=" + jwtToken;
        emailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), link);
        return jwtToken;
    }

    public void resetPassword(String token, String newPassword) {
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        userService.resetPassword(user, newPassword);
    }


    public void resendVerificationToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User already verified");
        }

        String jwtToken = jwtService.generateToken(new UserPrincipal(user));
        String link = frontendUrl + "auth/verify?token=" + jwtToken;
        emailService.sendVerificationEmail(user.getEmail(), username, link);
    }

}
