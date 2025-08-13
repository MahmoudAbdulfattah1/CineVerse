package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.UserPrincipal;
import com.cineverse.cineverse.exception.auth.InvalidCurrentPasswordException;
import com.cineverse.cineverse.exception.auth.OAuth2UserException;
import com.cineverse.cineverse.exception.auth.RegistrationException;
import com.cineverse.cineverse.exception.auth.UserNotFoundOrAuthenticatedException;
import com.cineverse.cineverse.exception.user.NoFieldsToUpdateException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CloudinaryService cloudinaryService;
    @InjectMocks
    private UserService userService;
    private User mockUser;
    private MultipartFile mockFile;
    private static final String PROFILE_PICTURES_FOLDER = "profile-pictures";

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mahmoud");
        mockUser.setEmail("mahhmoud@gmail.com");
        mockUser.setPassword("password123");
        mockUser.setEnabled(false);
        userService = spy(userService);

        mockFile = mock(MultipartFile.class);

    }

    @BeforeEach
    void setUpSecurityContext() {
        UserPrincipal userPrincipal = new UserPrincipal(mockUser);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        Collections.emptyList()
                );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameExists() {
        when(userRepository.existsByUsername(mockUser.getUsername())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.registerUser(mockUser));
        assertFalse(mockUser.isEnabled());
        verify(userRepository).existsByUsername(mockUser.getUsername());
        verify(userRepository, never()).existsByEmail(mockUser.getEmail());
    }

    @Test
    void registerUser_shouldThrowException_whenEmailExists() {
        when(userRepository.existsByUsername(mockUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.registerUser(mockUser));
        assertFalse(mockUser.isEnabled());
        verify(userRepository).existsByUsername(mockUser.getUsername());
        verify(userRepository).existsByEmail(mockUser.getEmail());
    }

    @Test
    void registerUser_shouldSaveUser_whenUsernameAndEmailAreUnique() {
        when(userRepository.existsByUsername(mockUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(mockUser);

        verify(userRepository).existsByUsername(mockUser.getUsername());
        verify(userRepository).existsByEmail(mockUser.getEmail());
        verify(passwordEncoder).encode("password123");
        assertEquals("encodedPassword", savedUser.getPassword());
        assertFalse(savedUser.isOauth2User());
    }

    @Test
    void updateUserProfile_shouldUpdateFields_whenValidFieldsProvided() {
        String newName = "New Name";
        String newBio = "New Bio";

        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User updatedUser = userService.updateUserProfile(newName, newBio, null);

        assertEquals(newName, updatedUser.getName());
        assertEquals(newBio, updatedUser.getBio());
        verify(userRepository).save(mockUser);
    }

    @Test
    void updateUserProfile_shouldThrowException_whenNoFieldsProvided() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);

        assertThrows(NoFieldsToUpdateException.class, () -> userService.updateUserProfile(null, null, null));
        verify(userRepository, never()).save(mockUser);
    }

    @Test
    void updateUserProfile_shouldThrowException_whenUserNotFound() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.updateUserProfile("name", "bio", null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetPassword_shouldUpdatePasswordAndEnableUser_whenUserIsNotEnabled() {
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.resetPassword(mockUser, newPassword);

        assertEquals(encodedPassword, mockUser.getPassword());
        assertTrue(mockUser.isEnabled());
        verify(userRepository).save(mockUser);
    }

    @Test
    void resetPassword_shouldUpdatePasswordOnly_whenUserIsAlreadyEnabled() {
        mockUser.setEnabled(true);
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.resetPassword(mockUser, newPassword);

        assertEquals(encodedPassword, mockUser.getPassword());
        assertTrue(mockUser.isEnabled());
        verify(userRepository).save(mockUser);
    }

    @Test
    void changeAuthenticatedUserPassword_shouldThrowException_whenUserNotFound() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(null);

        assertThrows(UserNotFoundOrAuthenticatedException.class, () -> userService.changeAuthenticatedUserPassword(1,
                "password123", "new"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeAuthenticatedUserPassword_shouldThrowException_whenUserIdDoesNotMatch() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);

        assertThrows(UserNotFoundOrAuthenticatedException.class, () -> userService.changeAuthenticatedUserPassword(2,
                "password123", "new"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeAuthenticatedUserPassword_shouldThrowException_whenUserIsOauthUser() {
        mockUser.setOauth2User(true);

        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);

        assertThrows(OAuth2UserException.class, () -> userService.changeAuthenticatedUserPassword(1, "password123",
                "new"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeAuthenticatedUserPassword_shouldThrowException_whenPasswordDoesNotMatch() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCurrentPasswordException.class, () -> userService.changeAuthenticatedUserPassword(
                1, "password123", "new"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeAuthenticatedUserPassword_shouldUpdatePassword_whenAllConditionsMet() {
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword";

        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.changeAuthenticatedUserPassword(1, mockUser.getPassword(), newPassword);

        assertEquals(encodedNewPassword, mockUser.getPassword());
        verify(userRepository).save(mockUser);
    }

    @Test
    void updateProfilePicture_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateProfilePicture(1, mockFile));
        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateProfilePicture_shouldThrowException_whenFileIsNull() {
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalArgumentException.class, () -> userService.updateProfilePicture(1, null));
        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateProfilePicture_shouldUpdateProfilePicture_whenFileIsValid() throws Exception {
        String imageUuid = "image-uuid";
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(cloudinaryService.uploadImage(mockFile, PROFILE_PICTURES_FOLDER)).thenReturn(imageUuid);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User updatedUser = userService.updateProfilePicture(1, mockFile);

        assertEquals(imageUuid, updatedUser.getProfilePictureUuid());
        verify(userRepository).findById(1);
        verify(cloudinaryService).uploadImage(mockFile, PROFILE_PICTURES_FOLDER);
        verify(userRepository).save(mockUser);
    }

    @Test
    void removeProfilePicture_shouldThrowException_whenUserNotFound() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.removeProfilePicture(1));
        verify(userRepository).findById(1);
        verify(cloudinaryService, never()).deleteImage(anyString(), anyString());
    }

    @Test
    void removeProfilePicture_shouldDeleteImageAndUpdateUser_whenUserFound() throws IOException {
        String imageUuid = "image-uuid";
        mockUser.setProfilePictureUuid(imageUuid);
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(cloudinaryService.deleteImage(imageUuid, PROFILE_PICTURES_FOLDER)).thenReturn(true);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.removeProfilePicture(1);

        assertNull(mockUser.getProfilePictureUuid());
        verify(userRepository).findById(1);
        verify(cloudinaryService).deleteImage(imageUuid, PROFILE_PICTURES_FOLDER);
        verify(userRepository).save(mockUser);
    }

    @Test
    void deleteUser_shouldDeleteUserAndProfilePicture_whenUserFound() throws IOException {
        String imageUuid = "image-uuid";
        mockUser.setProfilePictureUuid(imageUuid);
        when(cloudinaryService.deleteImage(imageUuid, PROFILE_PICTURES_FOLDER)).thenReturn(true);
        doNothing().when(userRepository).delete(mockUser);

        userService.deleteUser(mockUser);

        verify(cloudinaryService).deleteImage(imageUuid, PROFILE_PICTURES_FOLDER);
        verify(userRepository).delete(mockUser);
    }
}
