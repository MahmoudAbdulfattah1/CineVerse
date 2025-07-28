package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.domain.enums.ReactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReviewDto {
    private int reviewId;
    private int rate;
    private String title;
    private String description;
    private long likeCount;
    private long dislikeCount;
    private boolean spoiler;
    private int contentId;
    private ContentType contentType;
    private String contentTitle;
    private String contentPosterUrl;
    private ReactionType userReaction;
    private LocalDateTime createdAt;
}
