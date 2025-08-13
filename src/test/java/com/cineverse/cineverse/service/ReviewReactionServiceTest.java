package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.entity.ReviewReaction;
import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.enums.ReactionType;
import com.cineverse.cineverse.exception.review.ReactionAlreadyExistsException;
import com.cineverse.cineverse.exception.review.ReactionNotFoundException;
import com.cineverse.cineverse.exception.review.ReviewNotFoundException;
import com.cineverse.cineverse.repository.ReviewReactionRepository;
import com.cineverse.cineverse.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewReactionServiceTest {
    @Mock
    private ReviewService reviewService;
    @Mock
    private UserService userService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewReactionRepository reviewReactionRepository;
    @InjectMocks
    private ReviewReactionService reviewReactionService;
    private User mockUser;
    private Review mockReview;
    private ReviewReaction mockReviewReaction;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mahmoud");

        mockReview = new Review();
        mockReview.setId(1);
        mockReview.setUser(mockUser);
        mockReview.setLikeCount(1);
        mockReview.setDislikeCount(0);

        mockReviewReaction = new ReviewReaction();
        mockReviewReaction.setUser(mockUser);
        mockReviewReaction.setReview(mockReview);
        mockReviewReaction.setReactionType(ReactionType.LIKE);
    }

    @Test
    void reactToReview_shouldThrowException_whenReviewNotFound() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(null);

        assertThrows(ReviewNotFoundException.class, () -> reviewReactionService.reactToReview(1, ReactionType.LIKE, 1));
        verify(reviewService).getReviewById(1);
        verify(userService).getById(1);
        verify(reviewReactionRepository, never()).findByUserIdAndReviewId(1, 1);
    }

    @Test
    void reactToReview_shouldThrowException_whenExistingReactionIsNullAndNewTypeIsUndo() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(mockReview);
        when(reviewReactionRepository.findByUserIdAndReviewId(1, 1)).thenReturn(null);

        assertThrows(ReactionNotFoundException.class, () -> reviewReactionService.reactToReview(
                1, ReactionType.UNDO, 1));

        verify(reviewService).getReviewById(1);
        verify(userService).getById(1);
        verify(reviewReactionRepository).findByUserIdAndReviewId(1, 1);
        verify(reviewRepository, never()).save(any(Review.class));
        verify(reviewReactionRepository, never()).save(any(ReviewReaction.class));
    }

    @Test
    void reactToReview_shouldThrowException_whenUserHasAlreadyReactedWithThisType() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(mockReview);
        when(reviewReactionRepository.findByUserIdAndReviewId(1, 1)).thenReturn(mockReviewReaction);

        assertThrows(ReactionAlreadyExistsException.class, () -> reviewReactionService.reactToReview(
                1, ReactionType.LIKE, 1));

        verify(reviewService).getReviewById(1);
        verify(userService).getById(1);
        verify(reviewReactionRepository).findByUserIdAndReviewId(1, 1);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void reactToReview_shouldUpdateCountersAndSaveNewReaction_whenNewReactionIsLike() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(mockReview);
        when(reviewReactionRepository.findByUserIdAndReviewId(1, 1)).thenReturn(null);

        reviewReactionService.reactToReview(1, ReactionType.LIKE, 1);

        verify(reviewRepository).save(mockReview);
        verify(reviewReactionRepository).save(any(ReviewReaction.class));
        assertEquals(2, mockReview.getLikeCount());
        assertEquals(0, mockReview.getDislikeCount());
        assertEquals(ReactionType.LIKE, mockReviewReaction.getReactionType());
    }

    @Test
    void reactToReview_shouldUpdateCountersAndSaveExistingReaction_whenNewReactionIsDislike() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(mockReview);
        when(reviewReactionRepository.findByUserIdAndReviewId(1, 1)).thenReturn(null);

        reviewReactionService.reactToReview(1, ReactionType.DISLIKE, 1);

        verify(reviewRepository).save(mockReview);
        verify(reviewReactionRepository).save(any(ReviewReaction.class));
        assertEquals(1, mockReview.getLikeCount());
        assertEquals(1, mockReview.getDislikeCount());
    }

    @Test
    void reactToReview_shouldUpdateCountersAndSaveExistingReaction_whenNewReactionIsUndo() {
        when(userService.getById(1)).thenReturn(mockUser);
        when(reviewService.getReviewById(1)).thenReturn(mockReview);
        when(reviewReactionRepository.findByUserIdAndReviewId(1, 1)).thenReturn(mockReviewReaction);

        reviewReactionService.reactToReview(1, ReactionType.UNDO, 1);

        verify(reviewRepository).save(mockReview);
        verify(reviewReactionRepository).delete(mockReviewReaction);
        assertEquals(0, mockReview.getLikeCount());
        assertEquals(0, mockReview.getDislikeCount());
    }
}
