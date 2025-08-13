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

    /**
     * Retrieves a paginated list of watchlist items for a given user and watching status.
     *
     * @param username       the username of the user
     * @param watchingStatus the watching status (TO_WATCH or WATCHED)
     * @param page           the page number (0-based)
     * @param size           the size of the page
     * @return a {@link Page} of {@link WatchlistProjection} objects
     * @throws UserNotFoundException      if the user does not exist
     * @throws WatchlistNotFoundException if the user has no watchlist entries
     * @throws IllegalArgumentException   if the watching status is not TO_WATCH or WATCHED
     */
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

    /**
     * Creates a new watchlist entry.
     *
     * @param watchlist the {@link Watchlist} entity to save
     * @throws ContentNotFoundException                 if the content does not exist
     * @throws ContentAlreadyExistsInWatchlistException if the content is already in the user's watchlist
     */
    public void createWatchlistEntry(Watchlist watchlist) {
        if (!contentRepository.existsById(watchlist.getContent().getId())) {
            throw new ContentNotFoundException("Content not found");
        }
        if (watchlistRepository.existsByUserAndContent(watchlist.getUser(), watchlist.getContent())) {
            throw new ContentAlreadyExistsInWatchlistException("This content already exists in watchlist.");
        }
        watchlistRepository.save(watchlist);
    }

    /**
     * Updates the watching status of an existing watchlist entry.
     *
     * @param watchlistId    the ID of the watchlist entry
     * @param userId         the ID of the user making the update
     * @param watchingStatus the new watching status (TO_WATCH or WATCHED)
     * @throws IllegalArgumentException    if the watching status is invalid
     * @throws WatchlistNotFoundException  if the watchlist entry is not found
     * @throws UnauthorizedAccessException if the user does not own the watchlist entry
     */
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

    /**
     * Deletes a watchlist entry by its ID.
     *
     * @param watchlistId the ID of the watchlist entry
     * @param userId      the ID of the user making the deletion
     * @throws WatchlistNotFoundException  if the watchlist entry does not exist
     * @throws UnauthorizedAccessException if the user does not own the watchlist entry
     */
    public void deleteWatchlistEntry(int watchlistId, int userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException("Watchlist item not found"));

        if (watchlist.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not authorized to delete this watchlist item");
        }

        watchlistRepository.delete(watchlist);
    }

    /**
     * Retrieves the watchlist entry ID for a given user and content.
     *
     * @param userId    the ID of the user
     * @param contentId the ID of the content
     * @return the watchlist entry ID
     * @throws UserNotFoundException    if the user does not exist
     * @throws ContentNotFoundException if the content does not exist
     */
    public Integer getWatchlistIdByUserIdAndContentId(int userId, int contentId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        if (!contentRepository.existsById(contentId)) {
            throw new ContentNotFoundException("Content not found");
        }
        return watchlistRepository.getIdByUserIdAndContentId(userId, contentId);
    }

    /**
     * Returns the count of watchlist items for a given username.
     *
     * @param username the username of the user whose watchlist count is requested
     * @return the total number of watchlist items associated with the specified user
     * @throws IllegalArgumentException if the username is null or empty
     */
    public int getUserWatchlistCount(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return watchlistRepository.countByUsername(username);
    }


}
