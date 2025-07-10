package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.dto.auth.UserProfileDto;

public class UserMapper {
    public static UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .dateOfBirth(user.getDateOfBirth())
                .profilePicture(user.getProfilePictureUuid())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
