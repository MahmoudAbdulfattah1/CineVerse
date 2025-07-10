package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class VerificationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String verifyToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.isEnabled()) {
                return "User already verified.";
            }

            user.setEnabled(true);
            userRepository.save(user);

            return " Email verified successfully!";
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException("Verification link has expired.");
        } catch (JwtException e) {
            throw new IllegalStateException("Invalid verification link.");
        }
    }
}
