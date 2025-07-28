package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;

import java.time.LocalDateTime;

public interface UserReviewView {
    int getId();
    int getRate();
    String getTitle();
    String getDescription();
    long getLikeCount();
    long getDislikeCount();
    boolean isSpoiler();
    int getContentId();
    ContentType getContentType();
    String getContentTitle();
    String getContentPosterPath();
    LocalDateTime getCreatedAt();
}
