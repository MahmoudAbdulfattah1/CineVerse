package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Watchlist;
import com.cineverse.cineverse.domain.enums.WatchingStatus;
import com.cineverse.cineverse.dto.watchlist.WatchlistItemDto;
import com.cineverse.cineverse.dto.watchlist.WatchlistProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WatchlistMapper {
    @Value("${base.image.url}")
    private String contentImageBaseUrl;

    public WatchlistItemDto toWatchlistItemDto(WatchlistProjection watchlist) {

        return WatchlistItemDto.builder()
                .id(watchlist.getId())
                .contentId(watchlist.getContentId())
                .title(watchlist.getTitle())
                .overview(watchlist.getOverview())
                .contentPoster(contentImageFullPath(watchlist.getContentPoster()))
                .contentType(watchlist.getContentType())
                .imdbRate(watchlist.getImdbRate())
                .watchingStatus(watchlist.getWatchingStatus())
                .createdAt(watchlist.getCreatedAt())
                .updatedAt(watchlist.getUpdatedAt())
                .build();

    }

    public Watchlist toEntity(int contentId) {
        Watchlist watchlist = new Watchlist();
        Content content = new Content();
        content.setId(contentId);
        watchlist.setContent(content);
        watchlist.setWatchingStatus(
                WatchingStatus.TO_WATCH
        );
        return watchlist;
    }

    private String contentImageFullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return contentImageBaseUrl + path;
    }

}
