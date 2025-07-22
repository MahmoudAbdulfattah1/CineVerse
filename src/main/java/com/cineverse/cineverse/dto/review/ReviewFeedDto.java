package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.domain.enums.ReactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewFeedDto {
    private ReviewerDto user;
    private int reviewId;
    private int rate;
    private String title;
    private String description;
    private long likeCount;
    private long dislikeCount;
    private boolean spoiler;
    private ReactionType userReaction;
    private int contentId;
    private ContentType contentType;
    private String contentTitle;
    private String contentPosterPath;
    private LocalDateTime createdAt;

    public ReviewFeedDto(ReviewerDto user, int reviewId, int rate, String title, String description, long likeCount,
                         long dislikeCount, boolean spoiler, ReactionType userReaction,
                         int contentId, ContentType contentType, String contentTitle, String contentPosterPath,
                         LocalDateTime createdAt) {
        this.user = user;
        this.reviewId = reviewId;
        this.rate = rate;
        this.title = title;
        this.description = description;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.spoiler = spoiler;
        this.userReaction = userReaction;
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.contentPosterPath = contentPosterPath;
        this.createdAt = createdAt;
    }
}
