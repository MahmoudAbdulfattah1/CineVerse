package com.cineverse.cineverse.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
    private LocalDate dateOfBirth;

}