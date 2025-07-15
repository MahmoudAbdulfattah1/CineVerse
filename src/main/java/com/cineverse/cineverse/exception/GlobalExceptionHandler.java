package com.cineverse.cineverse.exception;

import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.exception.auth.*;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.content.UnsupportedContentTypeException;
import com.cineverse.cineverse.exception.crew.CrewMemberNotFoundException;
import com.cineverse.cineverse.exception.crew.SocialLinksNotFoundException;
import com.cineverse.cineverse.exception.global.BadRequestException;
import com.cineverse.cineverse.exception.global.InternalServerErrorException;
import com.cineverse.cineverse.exception.user.NoFieldsToUpdateException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleContentNotFoundException(ContentNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UnsupportedContentTypeException.class)
    public ResponseEntity<ApiResponse> handleUnsupportedContentTypeException(UnsupportedContentTypeException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CrewMemberNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCrewMemberNotFoundException(CrewMemberNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(SocialLinksNotFoundException.class)
    public ResponseEntity<ApiResponse> handleSocialLinksNotFoundException(SocialLinksNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse> handleInvalidUsernameOrPasswordException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Verification failed: Verification link has expired."));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Verification failed: Invalid verification link."));
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyVerifiedException(UserAlreadyVerifiedException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidOrExpiredTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidOrExpiredTokenException(InvalidOrExpiredTokenException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ApiResponse> handleRegistrationException(RegistrationException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(NoFieldsToUpdateException.class)
    public ResponseEntity<ApiResponse> handleNoFieldsToUpdateException(NoFieldsToUpdateException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundOrAuthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundOrAuthenticatedException(UserNotFoundOrAuthenticatedException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<ApiResponse> handleInvalidCurrentPasswordException(InvalidCurrentPasswordException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(OAuth2UserException.class)
    public ResponseEntity<ApiResponse> handleOAuth2UserException(OAuth2UserException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> handleIOException(IOException ex) {
        return new ResponseEntity<>(
                ApiResponse.failure("File processing error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleOtherExceptions(Exception ex) {
        return new ResponseEntity<>(
                ApiResponse.failure("An unexpected error occurred: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
