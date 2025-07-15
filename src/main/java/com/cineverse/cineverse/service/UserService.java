package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.exception.auth.*;
import com.cineverse.cineverse.exception.global.BadRequestException;
import com.cineverse.cineverse.exception.global.InternalServerErrorException;
import com.cineverse.cineverse.exception.user.NoFieldsToUpdateException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private static final String PROFILE_PICTURES_FOLDER = "profile-pictures";

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RegistrationException("Username already exists!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RegistrationException("Email already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOauth2User(false);
        return userRepository.save(user);

    }

    public User findByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUserProfile(String name, String bio, LocalDate dateOfBirth) {
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (name == null && bio == null && dateOfBirth == null) {
            throw new NoFieldsToUpdateException("No fields to update");
        }
        if (name != null) user.setName(name);
        if (bio != null) user.setBio(bio);
        if (dateOfBirth != null) user.setDateOfBirth(dateOfBirth);
        return userRepository.save(user);
    }

    @Transactional
    public void resetPassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        if (!user.isEnabled())
            user.setEnabled(true);
        userRepository.save(user);
    }

    public void changeAuthenticatedUserPassword(int userId, String currentPassword, String newPassword) {
        User user = getCurrentAuthenticatedUser();
        if (user == null || user.getId() != userId) {
            throw new UserNotFoundOrAuthenticatedException("User not found or not authenticated");
        }
        if (user.isOauth2User()) {
            throw new OAuth2UserException("Password cannot be changed for OAuth2 users.");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser();
        }

        throw new UserNotAuthenticatedException("User is not authenticated");
    }

    @Transactional
    public User updateProfilePicture(int userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
            cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
        }

        String newImageUuid = cloudinaryService.uploadImage(file, PROFILE_PICTURES_FOLDER);
        user.setProfilePictureUuid(newImageUuid);
        return userRepository.save(user);
    }


    @Transactional
    public void removeProfilePicture(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
            try {
                cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
            } catch (IOException ex) {
                throw new InternalServerErrorException("Failed to delete profile picture: " + ex.getMessage());
            }
        }

        user.setProfilePictureUuid(null);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        String profilePictureUuid = user.getProfilePictureUuid();

        if (profilePictureUuid != null && !profilePictureUuid.isEmpty()) {
            try {
                cloudinaryService.deleteImage(profilePictureUuid, PROFILE_PICTURES_FOLDER);
            } catch (IOException e) {
                throw new InternalServerErrorException("Failed to clean up profile picture: " + e.getMessage());
            }
        }

        userRepository.delete(user);
    }

}