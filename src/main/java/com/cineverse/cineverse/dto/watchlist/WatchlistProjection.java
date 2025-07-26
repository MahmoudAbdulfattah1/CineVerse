package com.cineverse.cineverse.dto.watchlist;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.domain.enums.WatchingStatus;

import java.time.LocalDateTime;

public interface WatchlistProjection {
    int getId();
    int getContentId();
    String getTitle();
    String getOverview();
    String getContentPoster();
    ContentType getContentType();
    float getImdbRate();
    WatchingStatus getWatchingStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
