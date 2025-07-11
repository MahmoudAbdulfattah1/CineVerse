package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            throw new RuntimeException("Username already exists!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUserProfile(String name, String bio, LocalDate dateOfBirth) {
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (name == null && bio == null && dateOfBirth == null) {
            throw new RuntimeException("No fields to update");
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
            throw new RuntimeException("User not found or not authenticated");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser();
        }

        throw new RuntimeException("User is not authenticated");
    }

//    @Transactional
//    public User updateProfilePicture(int userId, MultipartFile file) throws IOException {
//
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            throw new IllegalArgumentException("User not found with ID: " + userId);
//        }
//
//        User user = userOptional.get();
//
//        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
//            boolean deleted = cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
//        }
//
//        String newImageUuid = cloudinaryService.uploadImage(file, PROFILE_PICTURES_FOLDER);
//        user.setProfilePictureUuid(newImageUuid);
//        User updatedUser = userRepository.save(user);
//        return updatedUser;
//    }

    @Transactional
    public User updateProfilePicture(int userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
            cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
        }

        String newImageUuid = cloudinaryService.uploadImage(file, PROFILE_PICTURES_FOLDER);
        user.setProfilePictureUuid(newImageUuid);
        return userRepository.save(user);
    }


//    @Transactional
//    public User removeProfilePicture(int userId) {
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        if (userOptional.isEmpty()) {
//            throw new IllegalArgumentException("User not found with ID: " + userId);
//        }
//
//        User user = userOptional.get();
//
//        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
//            try {
//                boolean deleted = cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
//            } catch (IOException ex) {
//                throw new RuntimeException("Failed to delete profile picture: " + ex.getMessage());
//            }
//        }
//
//        user.setProfilePictureUuid(null);
//        User updatedUser = userRepository.save(user);
//
//        return updatedUser;
//    }
@Transactional
public void removeProfilePicture(int userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

    if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
        try {
            cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete profile picture: " + ex.getMessage());
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
                throw new RuntimeException("Failed to cleanup profile picture: " + e.getMessage());
            }
        }

        userRepository.delete(user);
    }



//    @Transactional
//    public boolean deleteUser(int userId) {
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        if (userOptional.isEmpty()) {
//            throw new IllegalArgumentException("User not found with ID: " + userId);
//        }
//
//        User user = userOptional.get();
//
//        if (user.getProfilePictureUuid() != null && !user.getProfilePictureUuid().isEmpty()) {
//            try {
//                boolean deleted = cloudinaryService.deleteImage(user.getProfilePictureUuid(), PROFILE_PICTURES_FOLDER);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to cleanup profile picture: " + e.getMessage());
//            }
//        }
//
//        userRepository.delete(user);
//
//        return true;
//    }


}