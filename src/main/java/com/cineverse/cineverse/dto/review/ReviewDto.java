package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ReactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
