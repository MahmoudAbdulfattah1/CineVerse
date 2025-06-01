package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.dto.ReviewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("""
            SELECT new com.cineverse.cineverse.dto.ReviewDto(
                new com.cineverse.cineverse.dto.UserDto(u.id, u.name, u.profilePicture),
                r.id,
                r.rate,
                r.reviewTitle,
                r.description,
                SUM(CASE WHEN react.reactionType = com.cineverse.cineverse.domain.enums.ReactionType.LIKE THEN 1 ELSE 0 END),
                SUM(CASE WHEN react.reactionType = com.cineverse.cineverse.domain.enums.ReactionType.DISLIKE THEN 1 ELSE 0 END),
                r.spoiler,
                r.createdAt
            )
            FROM Review r
            JOIN r.user u
            LEFT JOIN r.reactions react
            WHERE r.content.id = :id
            GROUP BY r.id, u.id, u.name, u.profilePicture, r.rate, r.reviewTitle, r.description, r.spoiler, r.createdAt
            ORDER BY r.createdAt DESC
            """)
    List<ReviewDto> findContentReviews(@Param("id") int id);
}
