package com.cineverse.cineverse.mapper.review;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.enums.ReactionType;
import com.cineverse.cineverse.dto.review.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    @Value("${cloudinary.image.url}")
    private String userImageBaseUrl;
    @Value("${base.image.url}")
    private String contentImageBaseUrl;

    public ReviewDto toContentReviewDto(Review review, ReactionType userReaction) {
        return ReviewDto.builder()
                .reviewID(review.getId())
                .title(review.getReviewTitle())
                .rate(review.getRate())
                .description(review.getDescription())
                .likeCount(review.getLikeCount())
                .dislikeCount(review.getDislikeCount())
                .spoiler(review.getSpoiler())
                .userReaction(userReaction)
                .user(ReviewerDto.builder()
                        .userId(review.getUser().getId())
                        .name(review.getUser().getName())
                        .username(review.getUser().getUsername())
                        .imageUrl(userImageFullPath(review.getUser().getProfilePictureUuid()))
                        .build())
                .createdAt(review.getCreatedAt())
                .build();

    }

    public ReviewDto toContentReviewDto(Review review) {
        return ReviewDto.builder()
                .reviewID(review.getId())
                .title(review.getReviewTitle())
                .rate(review.getRate())
                .description(review.getDescription())
                .likeCount(review.getLikeCount())
                .dislikeCount(review.getDislikeCount())
                .spoiler(review.getSpoiler())
                .user(ReviewerDto.builder()
                        .userId(review.getUser().getId())
                        .name(review.getUser().getName())
                        .username(review.getUser().getUsername())
                        .imageUrl(userImageFullPath(review.getUser().getProfilePictureUuid()))
                        .build())
                .createdAt(review.getCreatedAt())
                .build();

    }


    public ReviewFeedDto toFeedDto(ReviewFeedView view, ReactionType userReaction) {
        return ReviewFeedDto.builder()
                .reviewId(view.getId())
                .title(view.getTitle())
                .rate(view.getRate())
                .description(view.getDescription())
                .likeCount(view.getLikeCount())
                .dislikeCount(view.getDislikeCount())
                .spoiler(view.isSpoiler())
                .userReaction(userReaction)
                .contentTitle(view.getContentTitle())
                .contentPosterPath(contentImageFullPath(view.getContentPosterPath()))
                .contentId(view.getContentId())
                .contentType(view.getContentType())
                .user(ReviewerDto.builder()
                        .userId(view.getUserId())
                        .username(view.getUsername())
                        .name(view.getName())
                        .imageUrl(userImageFullPath(view.getProfilePictureUuid()))
                        .build())
                .createdAt(view.getCreatedAt())
                .build();
    }

    public UserReviewDto toUserReviewDto(UserReviewView review, ReactionType userReaction) {
        return UserReviewDto.builder()
                .reviewId(review.getId())
                .rate(review.getRate())
                .title(review.getTitle())
                .description(review.getDescription())
                .likeCount(review.getLikeCount())
                .dislikeCount(review.getDislikeCount())
                .spoiler(review.isSpoiler())
                .contentId(review.getContentId())
                .contentType(review.getContentType())
                .contentTitle(review.getContentTitle())
                .contentPosterPath(contentImageFullPath(review.getContentPosterPath()))
                .userReaction(userReaction)
                .createdAt(review.getCreatedAt())
                .build();
    }

    public TopReviewersDto toTopReviewersDto(TopReviewerView view) {
        ReviewerDto reviewerDto = new ReviewerDto(
                view.getId(),
                view.getUsername(),
                view.getName(),
                userImageFullPath(view.getProfilePictureUuid())
        );

        return TopReviewersDto.builder()
                .user(reviewerDto)
                .reviewCount(view.getReviewCount().intValue())
                .averageRating(
                        view.getAverageRating() != null
                                ? view.getAverageRating()
                                : 0.0
                )
                .build();
    }

    public TopReviewedContentDto toTopReviewedContentDto(TopReviewedContentView view) {
        return TopReviewedContentDto.builder()
                .contentId(view.getId())
                .title(view.getTitle())
                .contentType(view.getContentType())
                .averageRate(view.getAverageRating())
                .reviewCount(view.getReviewCount())
                .build();
    }

    public Review toReviewEntity(CreateReviewDto createReviewDto) {
        Review review = new Review();
        review.setRate(createReviewDto.getRate());
        review.setReviewTitle(createReviewDto.getTitle());
        review.setDescription(createReviewDto.getDescription());
        review.setSpoiler(createReviewDto.isSpoiler());
        return review;
    }

    public Review toReviewEntity(UpdateReviewDto updateReviewDto) {
        Review review = new Review();
        review.setRate(updateReviewDto.getRate());
        review.setReviewTitle(updateReviewDto.getTitle());
        review.setDescription(updateReviewDto.getDescription());
        review.setSpoiler(updateReviewDto.isSpoiler());
        return review;
    }


    private String userImageFullPath(String uuid) {
        if (uuid == null || uuid.isBlank()) return null;
        return userImageBaseUrl + uuid + ".jpg";
    }

    private String contentImageFullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return contentImageBaseUrl + path;
    }
}
