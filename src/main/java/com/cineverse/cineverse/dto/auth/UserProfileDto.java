package com.cineverse.cineverse.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private int id;
    private String username;
    private String email;
    private String name;
    private String bio;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}