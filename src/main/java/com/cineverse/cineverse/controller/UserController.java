package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.auth.ChangePasswordRequest;
import com.cineverse.cineverse.dto.auth.UpdateProfileRequest;
import com.cineverse.cineverse.dto.auth.UserProfileDto;
import com.cineverse.cineverse.mapper.user.UserMapper;
import com.cineverse.cineverse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getCurrentUserProfile() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        UserProfileDto profileDto = userMapper.toProfileDto(currentUser);
        return ResponseEntity.ok(ApiResponse.success(profileDto, "Profile retrieved successfully"));
    }


    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateCurrentUserProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User updatedUser = userService.updateUserProfile(
                request.getName(),
                request.getBio(),
                request.getDateOfBirth()
        );
        UserProfileDto profileDto = userMapper.toProfileDto(updatedUser);
        return ResponseEntity.ok(ApiResponse.success(profileDto, "Profile updated successfully"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        userService.changeAuthenticatedUserPassword(currentUser.getId(), request.getCurrentPassword(),
                request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<ApiResponse> getUserProfileById(@PathVariable String username) {
        User user = userService.findByUsernameOrThrow(username);
        UserProfileDto profileDto = userMapper.toProfileDto(user);
        return ResponseEntity.ok(ApiResponse.success(profileDto, "User profile retrieved successfully"));
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<ApiResponse> updateProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        User user = userService.getCurrentAuthenticatedUser();
        User updatedUser = userService.updateProfilePicture(user.getId(), file);
        return ResponseEntity.ok(
                ApiResponse.success(
                        updatedUser.getProfilePictureUuid(),
                        "Profile picture updated successfully"
                )
        );
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<ApiResponse> removeProfilePicture() {
        User user = userService.getCurrentAuthenticatedUser();
        userService.removeProfilePicture(user.getId());
        return ResponseEntity.ok(
                ApiResponse.success(null, "Profile picture removed successfully")
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCurrentUser() {
        User user = userService.getCurrentAuthenticatedUser();
        userService.deleteUser(user);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }


}