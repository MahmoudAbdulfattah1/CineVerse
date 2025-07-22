package com.cineverse.cineverse.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopReviewersDto {
    private ReviewerDto user;
    private int reviewCount;
    private double averageRating;
}
