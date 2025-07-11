package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.auth.*;
import com.cineverse.cineverse.mapper.AuthMapper;
import com.cineverse.cineverse.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;
    private final VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = AuthMapper.mapToUser(request);
            authService.register(user);

            return ResponseEntity.ok(ApiResponse.success(null, "User registered successfully. Please verify your " +
                    "email."));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.failure("Registration failed: " + ex.getMessage())
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            String token = jwtService.generateToken(new UserPrincipal(user));
            AuthResponse response = AuthMapper.mapToAuthResponse(user, token, "Login successful");
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(
                ApiResponse.success(null, "Logout successful")
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals(
                "anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure("No authenticated user found"));
        }

        String username = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(username, "Current user retrieved successfully"));
    }


    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            String message = verificationService.verifyToken(token);
            return ResponseEntity.ok(ApiResponse.success(null, message));
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("Verification failed: " + ex.getMessage()));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse> resendVerification(@RequestBody ResendEmailVerificationRequest request) {
        System.out.println("Resending verification for user: " + request);
        try {
            authService.resendVerificationToken(request.getUsername());
            return ResponseEntity.ok(
                    ApiResponse.success(null, "Verification email resent successfully.")
            );

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.failure("Resend failed: " + ex.getMessage())
            );
        }
    }


    @PostMapping("/forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
            String token = authService.forgetPasswordToken(user);
            AuthResponse response = AuthMapper.mapToAuthResponse(
                    user, token, "Reset password email sent successfully."
            );

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("Reset password failed: " + ex.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully."));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.failure("Password reset failed: " + ex.getMessage())
            );
        }
    }


}