package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.auth.ChangePasswordRequest;
import com.cineverse.cineverse.dto.auth.UpdateProfileRequest;
import com.cineverse.cineverse.dto.auth.UserProfileDto;
import com.cineverse.cineverse.mapper.UserMapper;
import com.cineverse.cineverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getCurrentUserProfile() {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            UserProfileDto profileDto = userMapper.toProfileDto(currentUser);

            return ResponseEntity.ok(ApiResponse.success(profileDto, "Profile retrieved successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure("No authenticated user found"));
        }
    }


    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateCurrentUserProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            User updatedUser = userService.updateUserProfile(
                    request.getName(),
                    request.getBio(),
                    request.getDateOfBirth()
            );

            UserProfileDto profileDto = userMapper.toProfileDto(updatedUser);

            return ResponseEntity.ok(ApiResponse.success(profileDto, "Profile updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            userService.changeAuthenticatedUserPassword(currentUser.getId(), request.getCurrentPassword(),
                    request.getNewPassword());

            return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<ApiResponse> getUserProfileById(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserProfileDto profileDto = userMapper.toProfileDto(user);

            return ResponseEntity.ok(ApiResponse.success(profileDto, "User profile retrieved successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
        }
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<ApiResponse> updateProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.failure("File is required"));
            }

            User user = userService.getCurrentAuthenticatedUser();
            User updatedUser = userService.updateProfilePicture(user.getId(), file);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            updatedUser.getProfilePictureUuid(),
                            "Profile picture updated successfully"
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("Failed to upload image: " + e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("An unexpected error occurred"));
        }
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<ApiResponse> removeProfilePicture() {
        try {
            User user = userService.getCurrentAuthenticatedUser();
            userService.removeProfilePicture(user.getId());

            return ResponseEntity.ok(
                    ApiResponse.success(null, "Profile picture removed successfully")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("An unexpected error occurred"));
        }
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCurrentUser() {
        try {
            User user = userService.getCurrentAuthenticatedUser();
            userService.deleteUser(user);

            return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(ApiResponse.failure(e.getMessage()));
        }
    }


}