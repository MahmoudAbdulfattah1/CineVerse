package com.cineverse.cineverse.dto.review;

public interface TopReviewerView {
    Integer getId();
    String getName();
    String getUsername();
    String getProfilePictureUuid();
    Long getReviewCount();
    Double getAverageRating();
}
