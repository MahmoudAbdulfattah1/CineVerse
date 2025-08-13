package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.review.*;
import com.cineverse.cineverse.exception.auth.UnauthorizedAccessException;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.review.ReviewNotFoundException;
import com.cineverse.cineverse.exception.review.UserAlreadyReviewedException;
import com.cineverse.cineverse.exception.user.UserNotFoundException;
import com.cineverse.cineverse.mapper.ReviewMapper;
import com.cineverse.cineverse.repository.ContentRepository;
import com.cineverse.cineverse.repository.ReviewReactionRepository;
import com.cineverse.cineverse.repository.ReviewRepository;
import com.cineverse.cineverse.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private ReviewReactionRepository reviewReactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;
    private Content mockContent;
    private Review mockReview;
    private Review mockUpdatedReview;
    private User mockUser;

    private List<Review> mockReviews;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mahmoud");

        mockContent = new Content();
        mockContent.setId(1);
        mockContent.setContentType(ContentType.MOVIE);

        mockReview = new Review();
        mockReview.setId(1);
        mockReview.setContent(mockContent);
        mockReview.setUser(mockUser);
        mockReview.setReviewTitle("Review Title");

        mockUpdatedReview = mockReview;
        mockUpdatedReview.setReviewTitle("Updated Review Title");

        mockReviews = List.of(mockReview);

    }

    @Test
    void getReviewFeed_shouldSortByLikes_whenCallRepoWithLikeCountSort() {
        ReviewFeedView view = mock(ReviewFeedView.class);
        when(view.getId()).thenReturn(10);
        Page<ReviewFeedView> repoPage = new PageImpl<>(List.of(view));
        when(reviewRepository.findMovieAndSeriesReviewFeed(anyList(), any(Pageable.class)))
                .thenReturn(repoPage);
        when(reviewMapper.toFeedDto(eq(view), any())).thenReturn(new ReviewFeedDto());

        Page<ReviewFeedDto> result = reviewService.getReviewFeed(0, 5, "likes", null);

        assertEquals(1, result.getTotalElements());
        verify(reviewRepository).findMovieAndSeriesReviewFeed(
                eq(List.of(ContentType.MOVIE, ContentType.SERIES)),
                argThat(pageable -> pageable.getSort().iterator().next().getProperty().equals("likeCount"))
        );
        verify(reviewReactionRepository, never()).findByUserIdAndReviewIdIn(anyInt(), anyList());
    }


    @Test
    void getContentReview_shouldThrowException_whenContentNotFound() {
        when(contentRepository.existsById(1))
                .thenReturn(false);

        assertThrows(ContentNotFoundException.class,
                () -> reviewService.getContentReviews(1, 0, 10, "createdAt", null));
        verify(reviewRepository, never()).findContentReviews(anyInt(), any(Pageable.class));
    }

    @Test
    void getContentReviews_shouldReturnReviews_whenContentExists() {
        when(contentRepository.existsById(1))
                .thenReturn(true);
        when(reviewRepository.findContentReviews(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(mockReviews));

        Page<ReviewDto> result = reviewService.getContentReviews(1, 0, 10, "createdAt", null);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getContentReviews_shouldReturnEmptyPage_whenNoReviews() {
        when(contentRepository.existsById(1))
                .thenReturn(true);
        when(reviewRepository.findContentReviews(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<ReviewDto> result = reviewService.getContentReviews(1, 0, 10, "createdAt", null);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getTopReviewers_shouldThrowException_whenNoUserFound() {
        when(reviewRepository.findTopReviewersWithStats(any(Pageable.class)))
                .thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> reviewService.getTopReviewers(0, 10));
        verify(reviewRepository).findTopReviewersWithStats(
                any(Pageable.class));
    }

    @Test
    void getTopReviewers_shouldReturnTopReviewers_whenUsersExist() {
        when(reviewRepository.findTopReviewersWithStats(any(Pageable.class)))
                .thenReturn(List.of(mock(TopReviewerView.class)));

        List<TopReviewerView> result = reviewService.getTopReviewers(0, 10);

        assertEquals(1, result.size());
        verify(reviewRepository).findTopReviewersWithStats(
                any(Pageable.class));
    }

    @Test
    void getTopReviewedContent_shouldReturnTopReviewedContent_whenExists() {
        when(reviewRepository.findTopReviewedContentWithStats(any(Pageable.class)))
                .thenReturn(List.of(mock(TopReviewedContentView.class)));

        List<TopReviewedContentView> result = reviewService.getTopReviewedContent(0, 10);

        assertEquals(1, result.size());
        verify(reviewRepository).findTopReviewedContentWithStats(
                any(Pageable.class));
    }

    @Test
    void getTopReviewedContent_shouldThrowException_whenNoContentFound() {
        when(reviewRepository.findTopReviewedContentWithStats(any(Pageable.class)))
                .thenReturn(List.of());

        assertThrows(ContentNotFoundException.class, () -> reviewService.getTopReviewedContent(0, 10));
        verify(reviewRepository).findTopReviewedContentWithStats(
                any(Pageable.class));
    }

    @Test
    void getUserReviews_shouldThrowException_whenUserNotFound() {
        when(userRepository.existsByUsername("wael"))
                .thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> reviewService.getUserReviews("wael", 0, 10, null));
        verify(reviewRepository, never()).findByUsername(anyString(), any(Pageable.class));
    }

    @Test
    void getUserReviews_shouldReturnUserReviews_whenUserExists() {
        UserReviewView view = mock(UserReviewView.class);
        when(view.getId()).thenReturn(1);
        Page<UserReviewView> repoPage = new PageImpl<>(List.of(view));
        when(userRepository.existsByUsername("mahmoud"))
                .thenReturn(true);
        when(reviewRepository.findByUsername(eq("mahmoud"), any(Pageable.class)))
                .thenReturn(repoPage);
        when(reviewMapper.toUserReviewDto(any(UserReviewView.class), any()))
                .thenReturn(new UserReviewDto());
        when(userRepository.existsByUsername("mahmoud"))
                .thenReturn(true);


        Page<UserReviewDto> result = reviewService.getUserReviews("mahmoud", 0, 10, null);

        verify(reviewRepository).findByUsername(eq("mahmoud"), any(Pageable.class));
        verify(reviewMapper, times(1)).toUserReviewDto(any(UserReviewView.class), any());
        verify(userRepository, times(1)).existsByUsername("mahmoud");
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void createReview_shouldThrowException_whenContentNotFound() {
        when(contentRepository.existsById(1))
                .thenReturn(false);

        assertThrows(ContentNotFoundException.class, () -> reviewService.createReview(mockReview));
        verify(contentRepository, times(1)).existsById(1);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_shouldThrowException_whenUserAlreadyReviewed() {
        when(contentRepository.existsById(1))
                .thenReturn(true);
        when(reviewRepository.existsByUserIdAndContentId(1, 1))
                .thenReturn(true);

        assertThrows(UserAlreadyReviewedException.class, () -> reviewService.createReview(mockReview));
        verify(contentRepository, times(1)).existsById(1);
        verify(reviewRepository, times(1)).existsByUserIdAndContentId(1, 1);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_shouldSaveReview_whenValid() {
        when(contentRepository.existsById(1))
                .thenReturn(true);
        when(reviewRepository.existsByUserIdAndContentId(1, 1))
                .thenReturn(false);
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(mockReview);

        assertSame(mockReview, reviewService.createReview(mockReview));
        assertEquals(1, mockReview.getId());
        assertEquals(mockContent, mockReview.getContent());
        verify(contentRepository, times(1)).existsById(1);
        verify(reviewRepository, times(1)).existsByUserIdAndContentId(1, 1);
        verify(reviewRepository, times(1)).save(mockReview);
    }

    @Test
    void updateReview_shouldThrowException_whenReviewNotFound() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(1, mockUpdatedReview, 1));
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void updateReview_shouldThrowException_whenUnauthorizedAccess() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(mockReview));

        assertThrows(UnauthorizedAccessException.class, () -> reviewService.updateReview(1, mockUpdatedReview, 2));
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void updateReview_shouldUpdateReview_whenValid() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(mockReview));
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(mockUpdatedReview);

        String expectedTitle = "Updated Review Title";
        Review result = reviewService.updateReview(1, mockUpdatedReview, 1);

        assertEquals(expectedTitle, result.getReviewTitle());
        assertEquals(mockReview.getId(), result.getId());
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).save(mockUpdatedReview);
    }

    @Test
    void deleteReview_shouldThrowException_whenReviewNotFound() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(1, 1));
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_shouldThrowException_whenUnauthorized() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(mockReview));
        assertThrows(UnauthorizedAccessException.class, () -> reviewService.deleteReview(1, 2));
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_shouldDeleteReview_whenValid() {
        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(mockReview));

        reviewService.deleteReview(mockReview.getId(), mockUser.getId());

        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).delete(mockReview);
    }
}
