package com.cineverse.cineverse.dto.user;

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
    private boolean isOauth2User;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}