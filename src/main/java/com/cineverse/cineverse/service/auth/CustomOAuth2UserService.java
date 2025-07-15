package com.cineverse.cineverse.service.auth;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.enums.Role;
import com.cineverse.cineverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
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
                            .enabled(true)
                            .role(Role.USER)
                            .isOauth2User(true)
                            .build();
                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oAuth2User.getAttributes(),
                "sub"
        );
    }
}