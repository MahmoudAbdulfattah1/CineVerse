package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.entity.ReviewReaction;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.domain.enums.ReactionType;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Retrieves a paginated feed of reviews for movies and series.
     *
     * @param page   the page number to retrieve (0-based)
     * @param size   the number of items per page
     * @param sortBy the field to sort by ("likes" for like count, otherwise created date)
     * @param userId optional ID of the currently logged-in user; used to fetch their reactions
     * @return a page of {@link ReviewFeedDto} representing the review feed
     */
    public Page<ReviewFeedDto> getReviewFeed(int page, int size, String sortBy, Integer userId) {
        Sort sort = "likes".equalsIgnoreCase(sortBy)
                ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewFeedView> views = reviewRepository.findMovieAndSeriesReviewFeed(
                List.of(ContentType.MOVIE, ContentType.SERIES),
                pageable
        );

        List<Integer> reviewIds = views.getContent().stream()
                .map(ReviewFeedView::getId)
                .toList();

        Map<Integer, ReactionType> userReactions = userId != null
                ? getUserReactionsForReviews(userId, reviewIds)
                : Collections.emptyMap();

        List<ReviewFeedDto> reviewFeedDto = views.getContent().stream()
                .map(view -> reviewMapper.toFeedDto(view, userReactions.get(view.getId())))
                .toList();

        return new PageImpl<>(reviewFeedDto, pageable, views.getTotalElements());
    }

    /**
     * Retrieves the reactions of a user for a list of review IDs.
     *
     * @param userId    the ID of the user
     * @param reviewIds the IDs of the reviews to check
     * @return a map where the key is the review ID and the value is the {@link ReactionType}
     */
    public Map<Integer, ReactionType> getUserReactionsForReviews(int userId, List<Integer> reviewIds) {
        List<ReviewReaction> reactions = reviewReactionRepository.findByUserIdAndReviewIdIn(userId, reviewIds);
        return reactions.stream()
                .collect(Collectors.toMap(
                        rr -> rr.getReview().getId(),
                        ReviewReaction::getReactionType
                ));
    }

    /**
     * Retrieves paginated reviews for a specific content item.
     *
     * @param contentId the ID of the content (movie, series, etc.)
     * @param page      the page number to retrieve (0-based)
     * @param size      the number of items per page
     * @param sortBy    the field to sort by ("likes" for like count, otherwise created date)
     * @param userId    optional ID of the current user; used to fetch their reactions
     * @return a page of {@link ReviewDto} for the given content
     * @throws ContentNotFoundException if the content does not exist
     */
    public Page<ReviewDto> getContentReviews(int contentId, int page, int size, String sortBy, Integer userId) {
        Sort sort = "likes".equalsIgnoreCase(sortBy)
                ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        if (!contentRepository.existsById(contentId)) {
            throw new ContentNotFoundException("Content not found");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Review> reviews = reviewRepository.findContentReviews(contentId, pageable);

        Map<Integer, ReactionType> userReactions = (userId != null)
                ? getUserReactionsForReviews(userId, reviews.stream().map(Review::getId).toList())
                : Collections.emptyMap();

        List<ReviewDto> contentReviewsDto = reviews.getContent().stream()
                .map(review -> reviewMapper.toContentReviewDto(review, userReactions.get(review.getId())))
                .toList();

        return new PageImpl<>(contentReviewsDto, pageable, reviews.getTotalElements());
    }

    /**
     * Retrieves a paginated list of top reviewers along with their statistics.
     *
     * @param page the page number to retrieve (0-based)
     * @param size the number of items per page
     * @return a list of {@link TopReviewerView}
     * @throws UserNotFoundException if no reviewers are found
     */
    public List<TopReviewerView> getTopReviewers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TopReviewerView> topReviewers = reviewRepository.findTopReviewersWithStats(pageable);
        if (topReviewers.isEmpty()) {
            throw new UserNotFoundException("No reviewers found");
        }
        return topReviewers;
    }

    /**
     * Retrieves a paginated list of top-reviewed content along with review statistics.
     *
     * @param page the page number to retrieve (0-based)
     * @param size the number of items per page
     * @return a list of {@link TopReviewedContentView}
     * @throws ContentNotFoundException if no reviewed content is found
     */
    public List<TopReviewedContentView> getTopReviewedContent(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TopReviewedContentView> topReviewedContent = reviewRepository.findTopReviewedContentWithStats(pageable);
        if (topReviewedContent.isEmpty()) {
            throw new ContentNotFoundException("No reviewed content found");
        }
        return topReviewedContent;
    }

    /**
     * Retrieves all reviews written by a specific user.
     *
     * @param username the username of the user
     * @param page     the page number to retrieve (0-based)
     * @param size     the number of items per page
     * @param userId   optional ID of the currently logged-in user; used to fetch their reactions
     * @return a page of {@link UserReviewDto}
     * @throws UserNotFoundException if the user does not exist
     */
    public Page<UserReviewDto> getUserReviews(String username, int page, int size, Integer userId) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User with username '" + username + "' does not exist");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<UserReviewView> reviews = reviewRepository.findByUsername(username, pageable);

        Map<Integer, ReactionType> userReactions = (userId != null)
                ? getUserReactionsForReviews(userId, reviews.stream().map(UserReviewView::getId).toList())
                : Collections.emptyMap();

        List<UserReviewDto> dtoList = reviews.getContent().stream()
                .map(review -> reviewMapper.toUserReviewDto(review, userReactions.get(review.getId())))
                .toList();

        return new PageImpl<>(dtoList, pageable, reviews.getTotalElements());
    }

    /**
     * Creates and saves a new review.
     *
     * @param review the review to save
     * @return the saved {@link Review}
     * @throws ContentNotFoundException     if the associated content does not exist
     * @throws UserAlreadyReviewedException if the user has already reviewed this content
     */
    public Review createReview(Review review) {
        int contentId = review.getContent().getId();
        int userId = review.getUser().getId();

        if (!contentRepository.existsById(contentId)) {
            throw new ContentNotFoundException("Content not found");
        }

        if (reviewRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new UserAlreadyReviewedException("User has already reviewed this content");
        }

        return reviewRepository.save(review);
    }

    /**
     * Updates an existing review.
     *
     * @param reviewId      the ID of the review to update
     * @param updatedReview the updated review details
     * @param userId        the ID of the user performing the update
     * @return the updated {@link Review}
     * @throws ReviewNotFoundException     if the review does not exist
     * @throws UnauthorizedAccessException if the user does not own the review
     */
    public Review updateReview(int reviewId, Review updatedReview, int userId) {
        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (existing.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not allowed to update this review");
        }

        existing.setReviewTitle(updatedReview.getReviewTitle());
        existing.setDescription(updatedReview.getDescription());
        existing.setSpoiler(updatedReview.getSpoiler());
        existing.setRate(updatedReview.getRate());
        existing.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(existing);
    }

    /**
     * Deletes a review.
     *
     * @param reviewId the ID of the review to delete
     * @param userId   the ID of the user performing the deletion
     * @throws ReviewNotFoundException     if the review does not exist
     * @throws UnauthorizedAccessException if the user does not own the review
     */
    public void deleteReview(int reviewId, int userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        if (review.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not allowed to delete this review");
        }
        reviewRepository.delete(review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId the ID of the review to retrieve
     * @return the {@link Review}
     * @throws ReviewNotFoundException if the review does not exist
     */
    public Review getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));
    }

    /**
     * Returns the count of reviews made by a given user.
     *
     * @param username the username of the user whose review count is requested
     * @return the total number of reviews submitted by the specified user
     * @throws IllegalArgumentException if the username is null or empty
     */
    public int getUserReviewCount(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return reviewRepository.countByUsername(username);
    }

}
