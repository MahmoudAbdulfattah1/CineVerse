package com.cineverse.cineverse.service.auth;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.enums.Role;
import com.cineverse.cineverse.repository.UserRepository;
import com.cineverse.cineverse.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final RestTemplate restTemplate;

    /**
     * Loads the OAuth2 user from the provider and handles user provisioning.
     * If the user does not exist, creates a new user, assigns a unique username,
     * saves the name and email, and triggers asynchronous upload of the user's
     * Google profile picture to Cloudinary.
     *
     * @param userRequest the OAuth2UserRequest containing client registration and access token
     * @return OAuth2User containing user attributes and authorities
     * @throws OAuth2AuthenticationException if an error occurs during user info loading
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profilePictureUrl = oAuth2User.getAttribute("picture");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String baseUsername = email.split("@")[0];
                    String uniqueUsername = baseUsername;
                    int suffix = 1;

                    while (userRepository.existsByUsername(uniqueUsername)) {
                        uniqueUsername = baseUsername + suffix++;
                    }

                    User newUser = User.builder()
                            .email(email)
                            .username(uniqueUsername)
                            .name(name)
                            .enabled(true)
                            .role(Role.USER)
                            .isOauth2User(true)
                            .build();
                    User savedUser = userRepository.save(newUser);

                    if (profilePictureUrl != null && !profilePictureUrl.isBlank()) {
                        uploadGoogleProfileImageAsync(savedUser, profilePictureUrl);
                    }
                    return savedUser;
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oAuth2User.getAttributes(),
                "sub"
        );
    }

    /**
     * Asynchronously downloads the user's Google profile picture from the given URL,
     * uploads it to Cloudinary, updates the user's profile picture UUID, and saves
     * the updated user entity.
     * This method is executed asynchronously using the configured task executor.
     *
     * @param user       the User entity to update
     * @param pictureUrl the URL of the Google profile picture
     */
    @Async("taskExecutor")
    public void uploadGoogleProfileImageAsync(User user, String pictureUrl) {
        try {
            byte[] imageBytes = restTemplate.getForObject(pictureUrl, byte[].class);
            if (imageBytes == null || imageBytes.length == 0) {
                log.warn("Downloaded image is empty for user {}", user.getEmail());
                return;
            }
            // use byte[] overload
            String uuid = cloudinaryService.uploadImageBytes(imageBytes, "profile.jpg", "profile-pictures");
            user.setProfilePictureUuid(uuid);
            userRepository.save(user);
        } catch (Exception e) {
            log.warn("Failed to download/upload google profile picture for {}: {}", user.getEmail(), e.getMessage());
        }
    }
}