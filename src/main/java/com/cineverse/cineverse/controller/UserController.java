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


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getCurrentUserProfile() {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser(); // same as used elsewhere
            UserProfileDto profileDto = UserMapper.toProfileDto(currentUser);

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

            UserProfileDto profileDto = UserMapper.toProfileDto(updatedUser);

            return ResponseEntity.ok(ApiResponse.success(profileDto, "Profile updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            userService.changeAuthenticatedUserPassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword());

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
            UserProfileDto profileDto = UserMapper.toProfileDto(user);

            return ResponseEntity.ok(ApiResponse.success(profileDto, "User profile retrieved successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
        }
    }

}