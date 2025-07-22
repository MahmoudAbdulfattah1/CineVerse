package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;

public interface TopReviewedContentView {
    Long getId();

    String getTitle();
    ContentType getContentType();

    Double getAverageRating();

    Integer getReviewCount();
}
