package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        if(!user.isEnabled())
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


}