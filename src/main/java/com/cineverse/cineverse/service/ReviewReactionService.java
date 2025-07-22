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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ReviewReactionService {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;


    @Transactional
    public void reactToReview(int reviewId, ReactionType newType, int userId) {
        User user = userService.getById(userId);
        Review review = reviewService.getReviewById(reviewId);
        if (review == null) {
            throw new ReviewNotFoundException("Review not found");
        }

        ReviewReaction existingReaction = reviewReactionRepository
                .findByUserIdAndReviewId(userId, reviewId);


        if (newType == ReactionType.UNDO) {
            if (existingReaction == null) {
                throw new ReactionNotFoundException("User has not reacted to this review");
            }
            updateCounters(review, existingReaction.getReactionType(), null);
            reviewReactionRepository.delete(existingReaction);
            reviewRepository.save(review);
            return;
        }

        if (existingReaction == null) {
            ReviewReaction newReaction = new ReviewReaction(user, review, newType);
            reviewReactionRepository.save(newReaction);
            updateCounters(review, null, newType);
        } else if (existingReaction.getReactionType() != newType) {
            updateCounters(review, existingReaction.getReactionType(), newType);
            existingReaction.setReactionType(newType);
            existingReaction.setUpdatedAt(LocalDate.now());
            reviewReactionRepository.save(existingReaction);
        } else
            throw new ReactionAlreadyExistsException("User has already reacted with this type");

        reviewRepository.save(review);
    }

    private void updateCounters(Review review, ReactionType oldType, ReactionType newType) {
        if (oldType == ReactionType.LIKE) review.setLikeCount(review.getLikeCount() - 1);
        if (oldType == ReactionType.DISLIKE) review.setDislikeCount(review.getDislikeCount() - 1);

        if (newType == ReactionType.LIKE) review.setLikeCount(review.getLikeCount() + 1);
        if (newType == ReactionType.DISLIKE) review.setDislikeCount(review.getDislikeCount() + 1);
    }
}
