package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ReviewReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Integer> {
    @Query("SELECT rr FROM ReviewReaction rr WHERE rr.user.id = :userId AND rr.review.id IN :reviewIds")
    List<ReviewReaction> findByUserIdAndReviewIdIn(@Param("userId") int userId, @Param("reviewIds") List<Integer> reviewIds);

    ReviewReaction findByUserIdAndReviewId(int userId, int reviewId);
}
