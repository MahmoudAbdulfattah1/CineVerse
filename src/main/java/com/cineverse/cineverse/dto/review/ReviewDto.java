package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ReactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewDto {
    private ReviewerDto user;
    private int reviewId;
    private int rate;
    private String title;
    private String description;
    private long likeCount;
    private long dislikeCount;
    private boolean spoiler;
    private ReactionType userReaction;
    private LocalDateTime createdAt;

    public ReviewDto(ReviewerDto user, int reviewId, int rate, String title, String description, long likeCount,
                     long dislikeCount, boolean spoiler, ReactionType userReaction, LocalDateTime createdAt) {
        this.user = user;
        this.reviewId = reviewId;
        this.rate = rate;
        this.title = title;
        this.description = description;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.spoiler = spoiler;
        this.userReaction = userReaction;
        this.createdAt = createdAt;
    }
}
