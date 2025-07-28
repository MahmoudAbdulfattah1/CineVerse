package com.cineverse.cineverse.dto.watchlist;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.domain.enums.WatchingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WatchlistItemDto {
    private int id;
    private int contentId;
    private String title;
    private String overview;
    private String contentPosterUrl;
    private ContentType contentType;
    private float imdbRate;
    private WatchingStatus watchingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
