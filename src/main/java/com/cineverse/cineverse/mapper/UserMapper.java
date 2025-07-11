package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.dto.auth.UserProfileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Value("${cloudinary.image.url}")
    private String imageBaseUrl;

    public UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .dateOfBirth(user.getDateOfBirth())
                .profilePicture(fullPath(user.getProfilePictureUuid()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private String fullPath(String uuid) {
        if (uuid == null || uuid.isBlank()) return null;
        return imageBaseUrl + uuid + ".jpg";
    }
}
