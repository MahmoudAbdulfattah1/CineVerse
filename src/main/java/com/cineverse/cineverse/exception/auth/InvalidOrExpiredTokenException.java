package com.cineverse.cineverse.exception.auth;

public class InvalidOrExpiredTokenException extends RuntimeException{
    public InvalidOrExpiredTokenException(String message) {
        super(message);
    }
}
