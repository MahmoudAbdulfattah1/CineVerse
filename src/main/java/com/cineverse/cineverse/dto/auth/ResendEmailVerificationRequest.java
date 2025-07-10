package com.cineverse.cineverse.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResendEmailVerificationRequest {
    @NotBlank(message = "Username is required")
    private String username;
}