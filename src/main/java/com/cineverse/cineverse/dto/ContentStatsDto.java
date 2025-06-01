package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentStatsDto {
    private int totalReviews;
    private int watchlistCount;

    public ContentStatsDto(int totalReviews, int watchlistCount) {
        this.totalReviews = totalReviews;
        this.watchlistCount = watchlistCount;
    }
}
