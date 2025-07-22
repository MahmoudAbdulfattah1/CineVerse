package com.cineverse.cineverse.exception.review;

public class UserAlreadyReviewedException extends RuntimeException {
    public UserAlreadyReviewedException(String message) {
        super(message);
    }

}
