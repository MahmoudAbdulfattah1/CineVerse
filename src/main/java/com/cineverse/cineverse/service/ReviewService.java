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

    public Map<Integer, ReactionType> getUserReactionsForReviews(int userId, List<Integer> reviewIds) {
        List<ReviewReaction> reactions = reviewReactionRepository.findByUserIdAndReviewIdIn(userId, reviewIds);
        return reactions.stream()
                .collect(Collectors.toMap(
                        rr -> rr.getReview().getId(),
                        ReviewReaction::getReactionType
                ));
    }

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

public List<TopReviewerView> getTopReviewers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    List<TopReviewerView> topReviewers = reviewRepository.findTopReviewersWithStats(pageable);
    if (topReviewers.isEmpty()) {
        throw new UserNotFoundException("No reviewers found");
    }
    return topReviewers;
}

    public List<TopReviewedContentView> getTopReviewedContent(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TopReviewedContentView> topReviewedContent = reviewRepository.findTopReviewedContentWithStats(pageable);
        if (topReviewedContent.isEmpty()) {
            throw new ContentNotFoundException("No reviewed content found");
        }
        return topReviewedContent;
    }

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

    public void deleteReview(int reviewId, int userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        if (review.getUser().getId() != userId) {
            throw new UnauthorizedAccessException("You are not allowed to delete this review");
        }
        reviewRepository.delete(review);
    }

    public Review getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));
    }

}
