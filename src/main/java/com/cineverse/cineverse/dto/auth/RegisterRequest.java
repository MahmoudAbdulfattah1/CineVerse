package com.cineverse.cineverse.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Pattern(regexp = "^[^\\s]+$", message = "Username must not contain spaces")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[\\w.-]+@((gmail|outlook|hotmail|yahoo)\\.com)$",
            message = "Only Gmail, Outlook, Hotmail or Yahoo emails are allowed"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String name;
}