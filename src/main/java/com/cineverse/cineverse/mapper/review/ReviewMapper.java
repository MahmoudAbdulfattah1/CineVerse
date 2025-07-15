package com.cineverse.cineverse.mapper.review;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.enums.ReactionType;
import com.cineverse.cineverse.dto.review.ReviewDto;
import com.cineverse.cineverse.dto.review.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {
    public List<ReviewDto> toDto(List<Review> reviews) {
        if (reviews == null) return List.of();
        return reviews.stream()
                .map(r -> new ReviewDto(
                        new UserDto(
                                r.getUser().getId(),
                                r.getUser().getName(),
                                r.getUser().getProfilePictureUuid()
                        ),
                        r.getId(),
                        r.getRate(),
                        r.getReviewTitle(),
                        r.getDescription(),
                        r.getReactions().stream().filter(type -> type.getReactionType().equals(ReactionType.LIKE)).count(),
                        r.getReactions().stream().filter(type -> type.getReactionType().equals(ReactionType.DISLIKE)).count(),
                        r.getSpoiler(),
                        r.getCreatedAt())
                )
                .toList();
    }
}
