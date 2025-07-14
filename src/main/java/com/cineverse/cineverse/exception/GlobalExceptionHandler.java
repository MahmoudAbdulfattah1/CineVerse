package com.cineverse.cineverse.exception;

import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.content.UnsupportedContentTypeException;
import com.cineverse.cineverse.exception.crew.CrewMemberNotFoundException;
import com.cineverse.cineverse.exception.crew.SocialLinksNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


}
