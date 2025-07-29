package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.enums.ReactionType;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.review.*;
import com.cineverse.cineverse.mapper.ReviewMapper;
import com.cineverse.cineverse.service.ReviewReactionService;
import com.cineverse.cineverse.service.ReviewService;
import com.cineverse.cineverse.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewReactionService reviewReactionService;
    private final UserService userService;
    private final ReviewMapper reviewMapper;

    @GetMapping
    public ResponseEntity<ApiResponse> getReviewFeed(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "recent") String sortBy) {

        User user = userService.getCurrentAuthenticatedUserOrNull();
        Integer userId = (user != null) ? user.getId() : null;

        Page<ReviewFeedDto> reviewFeed = reviewService.getReviewFeed(page, size, sortBy, userId);

        return ResponseEntity.ok(ApiResponse.success(reviewFeed, "Review feed fetched successfully"));
    }

    @GetMapping("/contents/{contentId}")
    public ResponseEntity<ApiResponse> getContentReviews(
            @PathVariable int contentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recent") String sortBy) {

        User user = userService.getCurrentAuthenticatedUserOrNull();
        Integer userId = (user != null) ? user.getId() : null;

        Page<ReviewDto> reviewFeed = reviewService.getContentReviews(contentId, page, size, sortBy, userId);

        return ResponseEntity.ok(
                ApiResponse.success(reviewFeed, "Content reviews fetched successfully")
        );
    }

    @GetMapping("/top-reviewers")
    public ResponseEntity<ApiResponse> getTopReviewers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
                ApiResponse.success(reviewService.getTopReviewers(page, size).stream()
                        .map(reviewMapper::toTopReviewersDto)
                        .toList(), "Top reviewers fetched successfully")
        );
    }

    @GetMapping("/top-reviewed")
    public ResponseEntity<ApiResponse> getTopReviewedContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
                ApiResponse.success(reviewService.getTopReviewedContent(page, size).stream()
                        .map(reviewMapper::toTopReviewedContentDto)
                        .toList(), "Top reviewed content fetched successfully")
        );
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<ApiResponse> getUserReviews(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.getCurrentAuthenticatedUserOrNull();
        Integer userId = (user != null) ? user.getId() : null;

        Page<UserReviewDto> userReviews = reviewService.getUserReviews(username, page, size, userId);

        return ResponseEntity.ok(
                ApiResponse.success(userReviews, "User reviews fetched successfully")
        );
    }


    @PostMapping
    public ResponseEntity<ApiResponse> createReview(
            @Valid @RequestBody CreateReviewDto createReviewDto) {

        User currentUser = userService.getCurrentAuthenticatedUser();
        Content content = new Content();
        content.setId(createReviewDto.getContentId());

        Review review = reviewMapper.toReviewEntity(createReviewDto);
        review.setUser(currentUser);
        review.setContent(content);

        Review createdReview = reviewService.createReview(review);
        ReviewDto reviewDto = reviewMapper.toContentReviewDto(createdReview);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(reviewDto, "Review created successfully"));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> updateReview(
            @PathVariable int reviewId,
            @Valid @RequestBody UpdateReviewDto updateReviewDto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        Review updatedData = reviewMapper.toReviewEntity(updateReviewDto);
        Review updatedReview = reviewService.updateReview(reviewId, updatedData, currentUser.getId());
        ReviewDto reviewDto = reviewMapper.toContentReviewDto(updatedReview);
        return ResponseEntity.ok(ApiResponse.success(reviewDto, "Review updated successfully"));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable int reviewId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        reviewService.deleteReview(reviewId, currentUser.getId());

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Review deleted successfully"
        ));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> getReviewById(@PathVariable int reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        ReviewDto reviewDto = reviewMapper.toContentReviewDto(review);
        return ResponseEntity.ok(ApiResponse.success(reviewDto, "Review retrieved successfully"));
    }

    @PutMapping("/{reviewId}/react")
    public ResponseEntity<ApiResponse> reactToReview(
            @PathVariable int reviewId,
            @RequestParam ReactionType type) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        reviewReactionService.reactToReview(reviewId, type, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Reaction updated successfully"
        ));
    }

}
