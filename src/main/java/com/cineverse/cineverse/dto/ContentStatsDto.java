package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentStatsDto {
    private int totalReviews;
    private int watchlistCount;
    private float platformRate;

    public ContentStatsDto(int totalReviews, int watchlistCount, float platformRate) {
        this.totalReviews = totalReviews;
        this.watchlistCount = watchlistCount;
        this.platformRate = platformRate;
    }
}
