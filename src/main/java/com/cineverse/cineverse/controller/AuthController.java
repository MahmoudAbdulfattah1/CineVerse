package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.auth.*;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.mapper.auth.AuthMapper;
import com.cineverse.cineverse.service.*;
import com.cineverse.cineverse.service.auth.AuthService;
import com.cineverse.cineverse.service.auth.JwtService;
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

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = AuthMapper.mapToUser(request);
        authService.register(user);
        return ResponseEntity.ok(ApiResponse.success(
                null, "User registered successfully. Please verify your email."));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        String token = jwtService.generateToken(new UserPrincipal(user));
        AuthResponse response = AuthMapper.mapToAuthResponse(user, token, "Login successful");
        return ResponseEntity.ok(ApiResponse.success(response));
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
        String message = authService.verifyToken(token);
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }


    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse> resendVerification(@RequestBody ResendEmailVerificationRequest request) {
        authService.resendVerificationToken(request.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success(null, "Verification email resent successfully.")
        );
    }


    @PostMapping("/forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        String token = authService.forgetPasswordToken(user);
        AuthResponse response = AuthMapper.mapToAuthResponse(
                user, token, "Reset password email sent successfully."
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully."));
    }


}