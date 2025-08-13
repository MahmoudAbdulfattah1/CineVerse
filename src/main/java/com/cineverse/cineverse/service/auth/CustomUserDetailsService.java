package com.cineverse.cineverse.service.auth;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Locates the user based on the username.
     *
     * <p>This method is called by the Spring Security framework during authentication.
     * It retrieves the {@link User} entity from the database using the provided username,
     * and wraps it in a {@link UserPrincipal} which implements
     * {@link org.springframework.security.core.userdetails.UserDetails}.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} object (specifically a {@link UserPrincipal})
     * if the user is found.
     * @throws UsernameNotFoundException if the user could not be found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);
    }
}
