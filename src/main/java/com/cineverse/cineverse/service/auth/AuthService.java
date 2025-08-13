package com.cineverse.cineverse.service.auth;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.exception.auth.InvalidOrExpiredTokenException;
import com.cineverse.cineverse.exception.auth.InvalidCredentialsException;
import com.cineverse.cineverse.exception.auth.UserAlreadyVerifiedException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.repository.UserRepository;
import com.cineverse.cineverse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Registers a new user, generates a verification JWT token,
     * constructs a verification link, and sends a verification email.
     *
     * @param user the User entity to register
     */
    public void register(User user) {
        User savedUser = userService.registerUser(user);
        String token = jwtService.generateToken(new UserPrincipal(savedUser));
        String link = frontendUrl + "/auth/verify?token=" + token;
        emailService.sendVerificationEmail(savedUser.getEmail(), user.getUsername(), link);
    }

    /**
     * Authenticates a user using their username and password.
     * If successful, returns the authenticated User entity.
     * Throws InvalidCredentialsException if authentication fails.
     *
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return the authenticated User entity
     * @throws InvalidCredentialsException if authentication fails due to invalid credentials
     */
    public User login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    /**
     * Generates a JWT token for password reset, constructs a reset password link,
     * sends the reset password email, and returns the generated token.
     *
     * @param user the User entity requesting password reset
     * @return the generated JWT token for password reset
     */
    public String forgetPasswordToken(User user) {
        String jwtToken = jwtService.generateToken(new UserPrincipal(user));
        String link = frontendUrl + "/auth/reset-password?token=" + jwtToken;
        emailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), link);
        return jwtToken;
    }

    /**
     * Resets the password for a user based on a JWT token and new password.
     * Validates the token and updates the user's password.
     *
     * @param token       the JWT token containing the username
     * @param newPassword the new password to set for the user
     * @throws InvalidOrExpiredTokenException if the JWT token is invalid or expired
     * @throws UserNotFoundException          if no user is found for the username in the token
     */
    public void resetPassword(String token, String newPassword) {
        String username;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            throw new InvalidOrExpiredTokenException("Invalid or expired token.");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userService.resetPassword(user, newPassword);
    }

    /**
     * Resends the email verification token for a user that is not yet verified.
     * Throws UserAlreadyVerifiedException if the user is already enabled.
     *
     * @param username the username of the user requesting a new verification email
     * @throws UserNotFoundException        if the user is not found
     * @throws UserAlreadyVerifiedException if the user is already verified
     */
    public void resendVerificationToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException("User already verified");
        }
        String jwtToken = jwtService.generateToken(new UserPrincipal(user));
        String link = frontendUrl + "/auth/verify?token=" + jwtToken;
        emailService.sendVerificationEmail(user.getEmail(), username, link);
    }

    /**
     * Verifies a JWT token sent during registration.
     * Enables the user account if the token is valid and the user is not already enabled.
     *
     * @param token the JWT verification token
     * @return a success message upon successful verification
     * @throws UserNotFoundException        if no user found for the username in the token
     * @throws UserAlreadyVerifiedException if the user is already enabled
     */
    public String verifyJwtTokenForRegistration(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException("User already verified");
        }
        user.setEnabled(true);
        userRepository.save(user);

        return "Email verified successfully!";
    }

}
