package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.Watchlist;
import com.cineverse.cineverse.domain.enums.WatchingStatus;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.watchlist.WatchlistItemDto;
import com.cineverse.cineverse.mapper.watchlist.WatchlistMapper;
import com.cineverse.cineverse.service.UserService;
import com.cineverse.cineverse.service.WatchlistService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchlist")
@AllArgsConstructor
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final UserService userService;
    private final WatchlistMapper watchlistMapper;

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getWatchlistByUserAndWatchingStatus(
            @PathVariable("username") String username,
            @RequestParam(defaultValue = "TO_WATCH") WatchingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<WatchlistItemDto> watchlistPage = watchlistService.getWatchlistByUserAndWatchingStatus(
                        username, status, page, size)
                .map(watchlistMapper::toWatchlistItemDto);
        return ResponseEntity.ok(ApiResponse.success(
                watchlistPage
                , "Watchlist fetched successfully"
        ));
    }


    @PostMapping
    public ResponseEntity<ApiResponse> addToWatchlist(@RequestParam int contentId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        Watchlist watchlist = watchlistMapper.toEntity(contentId);
        watchlist.setUser(currentUser);
        watchlistService.createWatchlistEntry(watchlist);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Content added to watchlist"));
    }

    @PutMapping("/{watchlistId}")
    public ResponseEntity<ApiResponse> updateWatchingStatus(
            @PathVariable int watchlistId,
            @RequestParam WatchingStatus status
    ) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        watchlistService.updateWatchingStatus(watchlistId, currentUser.getId(), status);
        return ResponseEntity.ok(ApiResponse.success(null, "Status updated successfully"));
    }

    @DeleteMapping("/{watchlistId}")
    public ResponseEntity<ApiResponse> deleteWatchlistEntry(@PathVariable int watchlistId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        watchlistService.deleteWatchlistEntry(watchlistId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Watchlist entry deleted successfully"));
    }

}
