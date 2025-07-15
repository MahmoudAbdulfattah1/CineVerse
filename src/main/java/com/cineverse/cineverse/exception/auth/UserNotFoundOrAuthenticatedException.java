package com.cineverse.cineverse.exception.auth;

public class UserNotFoundOrAuthenticatedException extends RuntimeException {
    public UserNotFoundOrAuthenticatedException(String message) {
        super(message);
    }
}
