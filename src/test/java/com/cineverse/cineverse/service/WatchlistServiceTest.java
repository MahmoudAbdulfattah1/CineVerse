package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Content;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceTest {
    @Mock
    private WatchlistRepository watchlistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ContentRepository contentRepository;
    @InjectMocks
    private WatchlistService watchlistService;
    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private User mockUser;
    private Content mockContent;
    private Watchlist mockWatchlist;


    @BeforeEach
    void setUpWatchlist() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mahmoud");

        mockContent = new Content();
        mockContent.setId(1);

        mockWatchlist = new Watchlist();
        mockWatchlist.setId(1);
        mockWatchlist.setUser(mockUser);
        mockWatchlist.setContent(mockContent);
        mockWatchlist.setWatchingStatus(WatchingStatus.TO_WATCH);

    }

    @Test
    void getWatchlistByUserAndWatchingStatus_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("wael")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> watchlistService.getWatchlistByUserAndWatchingStatus("wael",
                WatchingStatus.TO_WATCH, 0, 10));
        verify(watchlistRepository, never()).findWatchlistByUserIdAndStatus(
                anyInt(), any(WatchingStatus.class), any(Pageable.class));
    }

    @Test
    void getWatchlistByUserAndWatchingStatus_shouldThrowException_whenNoItems() {
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));
        when(watchlistRepository.existsByUserId(mockUser.getId())).thenReturn(false);

        assertThrows(WatchlistNotFoundException.class, () -> watchlistService.getWatchlistByUserAndWatchingStatus(
                "mahmoud", WatchingStatus.TO_WATCH, 0, 10));
        verify(watchlistRepository, never()).findWatchlistByUserIdAndStatus(
                anyInt(), any(WatchingStatus.class), any(Pageable.class));
    }

    @Test
    void getWatchlistByUserAndWatchingStatus_shouldThrowException_whenInvalidWatchingStatus() {
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));
        when(watchlistRepository.existsByUserId(mockUser.getId())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> watchlistService.getWatchlistByUserAndWatchingStatus(
                "mahmoud", null, 0, 10));
        verify(userRepository).findByUsername("mahmoud");
        verify(watchlistRepository).existsByUserId(1);
        verify(watchlistRepository, never()).findWatchlistByUserIdAndStatus(
                anyInt(), any(WatchingStatus.class), any(Pageable.class));
    }

    @Test
    void getWatchlistByUserAndWatchingStatus_shouldReturnPage_whenAllValid() {
        when(userRepository.findByUsername("mahmoud")).thenReturn(Optional.of(mockUser));
        when(watchlistRepository.existsByUserId(1)).thenReturn(true);

        WatchlistProjection watchlistProjection = mock(WatchlistProjection.class);
        Page<WatchlistProjection> page = new PageImpl<>(List.of(watchlistProjection), PageRequest.of(0, 2), 1);

        when(watchlistRepository.findWatchlistByUserIdAndStatus(eq(1), eq(WatchingStatus.TO_WATCH),
                any(Pageable.class)))
                .thenReturn(page);

        Page<WatchlistProjection> result = watchlistService.getWatchlistByUserAndWatchingStatus("mahmoud",
                WatchingStatus.TO_WATCH, 0, 2);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(watchlistRepository).findWatchlistByUserIdAndStatus(eq(1), eq(WatchingStatus.TO_WATCH),
                pageableCaptor.capture());
        Pageable passed = pageableCaptor.getValue();
        assertEquals(0, passed.getPageNumber());
        assertEquals(2, passed.getPageSize());
    }

    @Test
    void createWatchlistEntry_shouldThrowException_whenContentNotFound() {
        when(contentRepository.existsById(mockContent.getId())).thenReturn(false);

        assertThrows(ContentNotFoundException.class, () ->
                watchlistService.createWatchlistEntry(mockWatchlist)
        );
        verify(contentRepository).existsById(mockContent.getId());
        verify(contentRepository, times(1)).existsById(mockContent.getId());
        verify(watchlistRepository, never()).save(any(Watchlist.class));

    }

    @Test
    void createWatchlistEntry_shouldThrowException_whenContentAlreadyInWatchlist() {
        when(contentRepository.existsById(mockContent.getId())).thenReturn(true);
        when(watchlistRepository.existsByUserAndContent(mockUser, mockContent)).thenReturn(true);

        assertThrows(ContentAlreadyExistsInWatchlistException.class, () ->
                watchlistService.createWatchlistEntry(mockWatchlist)
        );
        verify(contentRepository).existsById(mockContent.getId());
        verify(watchlistRepository).existsByUserAndContent(mockUser, mockContent);
        verify(watchlistRepository, never()).save(any(Watchlist.class));
    }

    @Test
    void createWatchlistEntry_shouldCreateWatchlist_whenValid() {
        when(contentRepository.existsById(mockContent.getId())).thenReturn(true);
        when(watchlistRepository.existsByUserAndContent(mockUser, mockContent)).thenReturn(false);

        watchlistService.createWatchlistEntry(mockWatchlist);

        verify(contentRepository).existsById(mockContent.getId());
        verify(watchlistRepository).save(mockWatchlist);
    }

    @Test
    void updateWatchingStatus_shouldThrowException_whenInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                watchlistService.updateWatchingStatus(1, 1, null));
        verify(watchlistRepository, never()).findById(anyInt());
        verify(watchlistRepository, never()).save(any(Watchlist.class));
    }

    @Test
    void updateWatchingStatus_shouldThrowException_whenWatchlistNotFound() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(WatchlistNotFoundException.class, () ->
                watchlistService.updateWatchingStatus(1, 1, WatchingStatus.WATCHED));
        verify(watchlistRepository).findById(1);
        verify(watchlistRepository, never()).save(any(Watchlist.class));
    }

    @Test
    void updateWatchingStatus_shouldThrowException_whenUnauthorizedAccess() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.of(mockWatchlist));

        assertThrows(UnauthorizedAccessException.class, () ->
                watchlistService.updateWatchingStatus(1, 2, WatchingStatus.WATCHED));
        verify(watchlistRepository).findById(1);
        verify(watchlistRepository, never()).save(any(Watchlist.class));
    }

    @Test
    void updateWatchingStatus_shouldUpdateStatus_whenValid() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.of(mockWatchlist));

        watchlistService.updateWatchingStatus(mockWatchlist.getId(), mockUser.getId(), WatchingStatus.WATCHED);

        verify(watchlistRepository).findById(1);
        verify(watchlistRepository).save(mockWatchlist);
        assertEquals(WatchingStatus.WATCHED, mockWatchlist.getWatchingStatus());
    }

    @Test
    void deleteWatchlistEntry_shouldThrowException_whenWatchlistNotFound() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(WatchlistNotFoundException.class, () ->
                watchlistService.deleteWatchlistEntry(1, 1));
        verify(watchlistRepository).findById(1);
        verify(watchlistRepository, never()).delete(any(Watchlist.class));
    }

    @Test
    void deleteWatchlistEntry_shouldThrowException_whenUnauthorizedAccess() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.of(mockWatchlist));

        assertThrows(UnauthorizedAccessException.class, () ->
                watchlistService.deleteWatchlistEntry(1, 2));
        verify(watchlistRepository).findById(1);
        verify(watchlistRepository, never()).delete(any(Watchlist.class));
    }

    @Test
    void deleteWatchlistEntry_shouldDeleteEntry_whenValid() {
        when(watchlistRepository.findById(1)).thenReturn(Optional.of(mockWatchlist));

        watchlistService.deleteWatchlistEntry(mockWatchlist.getId(), mockUser.getId());

        verify(watchlistRepository).findById(1);
        verify(watchlistRepository).delete(mockWatchlist);
    }

    @Test
    void getWatchlistIdByUserIdAndContentId_shouldThrowException_whenUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                watchlistService.getWatchlistIdByUserIdAndContentId(1, 1));
        verify(userRepository).existsById(1);
        verify(contentRepository, never()).existsById(anyInt());
        verify(watchlistRepository, never()).getIdByUserIdAndContentId(anyInt(), anyInt());
    }

    @Test
    void getWatchlistIdByUserIdAndContentId_shouldThrowException_whenContentNotFound() {
        when(contentRepository.existsById(1)).thenReturn(false);
        when(userRepository.existsById(1)).thenReturn(true);

        assertThrows(ContentNotFoundException.class, () ->
                watchlistService.getWatchlistIdByUserIdAndContentId(1, 1));
        verify(contentRepository).existsById(1);
        verify(userRepository).existsById(1);
        verify(watchlistRepository, never()).getIdByUserIdAndContentId(anyInt(), anyInt());

    }

    @Test
    void getWatchlistIdByUserIdAndContentId_shouldRetrieveWatchlist_whenValid() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(contentRepository.existsById(1)).thenReturn(true);
        when(watchlistRepository.getIdByUserIdAndContentId(1, 1)).thenReturn(1);

        Integer result = watchlistService.getWatchlistIdByUserIdAndContentId(1, 1);

        assertNotNull(result);
        assertEquals(1, result.intValue());
        verify(userRepository).existsById(1);
        verify(contentRepository).existsById(1);
    }


}
