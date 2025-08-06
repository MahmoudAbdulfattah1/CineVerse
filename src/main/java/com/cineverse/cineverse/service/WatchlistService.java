package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.Watchlist;
import com.cineverse.cineverse.domain.enums.WatchingStatus;
import com.cineverse.cineverse.dto.watchlist.WatchlistProjection;
import com.cineverse.cineverse.exception.auth.UnauthorizedAccessException;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.exception.watchlist.ContentAlreadyExistsInWatchlistException;
import com.cineverse.cineverse.exception.watchlist.WatchlistNotFoundException;
import com.cineverse.cineverse.repository.ContentRepository;
import com.cineverse.cineverse.repository.UserRepository;
import com.cineverse.cineverse.repository.WatchlistRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    public Page<WatchlistProjection> getWatchlistByUserAndWatchingStatus(
            String username, WatchingStatus watchingStatus, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!watchlistRepository.existsByUserId(user.getId())) {
            throw new WatchlistNotFoundException("There are no watchlist items for you");
        }
        if (watchingStatus != WatchingStatus.TO_WATCH &&
                watchingStatus != WatchingStatus.WATCHED) {
            throw new IllegalArgumentException(
                    "Invalid watching status: " + watchingStatus + ". Allowed values are TO_WATCH or WATCHED.");
        }
        Pageable pageable = PageRequest.of(page, size);
        return watchlistRepository.findWatchlistByUserIdAndStatus(user.getId(), watchingStatus, pageable);
    }


    public void createWatchlistEntry(Watchlist watchlist) {
        if (!contentRepository.existsById(watchlist.getContent().getId())) {
            throw new ContentNotFoundException("Content not found");
        }
        if (watchlistRepository.existsByUserAndContent(watchlist.getUser(), watchlist.getContent())) {
            throw new ContentAlreadyExistsInWatchlistException("This content already exists in watchlist.");
        }
        watchlistRepository.save(watchlist);
    }

    public void updateWatchingStatus(int watchlistId, int userId, WatchingStatus watchingStatus) {
        if (watchingStatus != WatchingStatus.TO_WATCH &&
                watchingStatus != WatchingStatus.WATCHED) {
            throw new IllegalArgumentException(
                    "Invalid watching status: " + watchingStatus + ". Allowed values are TO_WATCH or WATCHED.");
        }
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException("Watchlist item is not found"));

        if (watchlist.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not authorized to modify this watchlist item");
        }

        watchlist.setWatchingStatus(watchingStatus);
        watchlistRepository.save(watchlist);
    }

    public void deleteWatchlistEntry(int watchlistId, int userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException("Watchlist item not found"));

        if (watchlist.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not authorized to delete this watchlist item");
        }

        watchlistRepository.delete(watchlist);
    }

    public Integer getWatchlistIdByUserIdAndContentId(int userId, int contentId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        if (!contentRepository.existsById(contentId)) {
            throw new ContentNotFoundException("Content not found");
        }
        return watchlistRepository.getIdByUserIdAndContentId(userId, contentId);
    }

}
