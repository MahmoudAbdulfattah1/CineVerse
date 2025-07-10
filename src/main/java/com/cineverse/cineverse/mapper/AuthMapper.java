package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.dto.auth.AuthResponse;
import com.cineverse.cineverse.dto.auth.RegisterRequest;

public class AuthMapper {

    public static User mapToUser(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build();
    }

    public static AuthResponse mapToAuthResponse(User user, String token, String message) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message(message)
                .build();
    }
}
