package com.cineverse.cineverse.service.auth;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.exception.auth.InvalidCredentialsException;
import com.cineverse.cineverse.exception.auth.RegistrationException;
import com.cineverse.cineverse.exception.auth.UserAlreadyVerifiedException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.repository.UserRepository;
import com.cineverse.cineverse.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;
    private User mockUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mahmoud");
        mockUser.setPassword("password");

        mockToken = "test-token";
        ReflectionTestUtils.setField(authService, "frontendUrl", "http://localhost:8080");
    }


    @Test
    void register_shouldSendVerificationEmail_whenUserRegistered() {
        String expectedLink = "http://localhost:8080/auth/verify?token=" + mockToken;
        when(userService.registerUser(mockUser)).thenReturn(mockUser);
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(mockToken);

        authService.register(mockUser);
        verify(userService).registerUser(mockUser);
        verify(jwtService).generateToken(any(UserPrincipal.class));
        verify(emailService).sendVerificationEmail(mockUser.getEmail(), mockUser.getUsername(), expectedLink);
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyVerified() {
        when(userService.registerUser(mockUser))
                .thenThrow(new RegistrationException("Username already exists!"));

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            authService.register(mockUser);
        });

        assertEquals("Username already exists!", exception.getMessage());
        verifyNoInteractions(jwtService, emailService);

    }

    @Test
    void login_shouldReturnUser_whenCredentialsAreValid() {
        UserPrincipal userPrincipal = new UserPrincipal(mockUser);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        User result = authService.login("mahmoud", "password");

        assertEquals(mockUser, result);
        assertEquals("mahmoud", result.getUsername());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login("mahmoud", "wrongpassword");
        });
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void forgetPassword_shouldSendResetPasswordEmail_whenUserExists() {
        String expectedLink = "http://localhost:8080/auth/reset-password?token=" + mockToken;
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(mockToken);
        doNothing().when(emailService).sendResetPasswordEmail(
                mockUser.getEmail(), mockUser.getUsername(), expectedLink);
        String token = authService.forgetPasswordToken(mockUser);
        assertEquals(mockToken, token);
    }

    @Test
    void forgetPassword_shouldThrowException_whenUserDoesNotExist() {
        when(jwtService.generateToken(any(UserPrincipal.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.forgetPasswordToken(mockUser);
        });
        verify(jwtService).generateToken(any(UserPrincipal.class));
        verify(emailService, never()).sendResetPasswordEmail(any(), any(), any());
    }

    @Test
    void resendVerificationToken_shouldSendEmail_whenUserExistsAndNotVerified() {
        mockUser.setEnabled(false);
        String expectedLink = "http://localhost:8080/auth/verify?token=" + mockToken;
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn(mockToken);
        doNothing().when(emailService).sendVerificationEmail(
                mockUser.getEmail(), mockUser.getUsername(), expectedLink);

        authService.resendVerificationToken("mahmoud");

        verify(userRepository).findByUsername("mahmoud");
        verify(jwtService).generateToken(any(UserPrincipal.class));
        verify(emailService).sendVerificationEmail(mockUser.getEmail(), mockUser.getUsername(), expectedLink);
    }

    @Test
    void resendVerificationToken_shouldThrowException_whenUserAlreadyVerified() {
        mockUser.setEnabled(true);
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));

        assertThrows(UserAlreadyVerifiedException.class, () -> {
            authService.resendVerificationToken("mahmoud");
        });
        verify(userRepository).findByUsername("mahmoud");
        verify(jwtService, never()).generateToken(any(UserPrincipal.class));
        verify(emailService, never()).sendVerificationEmail(any(), any(), any());
    }

    @Test
    void verifyJwtTokenForRegistration_shouldThrow_whenUserNotFoundException() {
        when(jwtService.extractUsername(mockToken)).thenReturn("nothing");
        when(userRepository.findByUsername("nothing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.verifyJwtTokenForRegistration(mockToken);
        });
        verify(jwtService).extractUsername(mockToken);
        verify(userRepository).findByUsername("nothing");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyJwtTokenForRegistration_shouldVerifyUser_whenUserExistsAndNotVerified() {
        mockUser.setEnabled(false);
        when(jwtService.extractUsername(mockToken)).thenReturn("mahmoud");
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));

        String result = authService.verifyJwtTokenForRegistration(mockToken);

        assertEquals("Email verified successfully!", result);
        verify(userRepository).findByUsername("mahmoud");
        verify(userRepository).save(mockUser);
    }
}
