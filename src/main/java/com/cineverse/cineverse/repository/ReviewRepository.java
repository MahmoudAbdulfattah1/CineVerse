package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.review.ReviewFeedView;
import com.cineverse.cineverse.dto.review.TopReviewedContentView;
import com.cineverse.cineverse.dto.review.TopReviewerView;
import com.cineverse.cineverse.dto.review.UserReviewView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("""
                SELECT r FROM Review r
                JOIN FETCH r.user u
                WHERE r.content.id = :contentId
            """)
    Page<Review> findContentReviews(@Param("contentId") int contentId, Pageable pageable);


    @Query("""
                SELECT u.id AS id,
                       u.username AS username,
                       u.name AS name,
                       u.profilePictureUuid AS profilePictureUuid,
                       COUNT(r) AS reviewCount,
                       AVG(r.rate) AS averageRating
                FROM Review r
                JOIN r.user u
                GROUP BY u.id, u.username, u.name, u.profilePictureUuid
                ORDER BY COUNT(r) DESC
            """)
    List<TopReviewerView> findTopReviewersWithStats(Pageable pageable);

    @Query("""
                SELECT c.id AS id,
                       c.title AS title,
                       c.contentType AS contentType,
                       COUNT(r) AS reviewCount,
                       AVG(r.rate) AS averageRating
                FROM Review r
                JOIN r.content c
                GROUP BY id, title, contentType
                ORDER BY COUNT(r) DESC
            """)
    List<TopReviewedContentView> findTopReviewedContentWithStats(Pageable pageable);

    @Query("""
            SELECT u.id AS userId,
                   u.username AS username,
                   u.name AS name,
                   u.profilePictureUuid AS profilePictureUuid,
                   r.id AS id,
                   r.rate AS rate,
                   r.reviewTitle AS title,
                   r.description AS description,
                   r.likeCount AS likeCount,
                   r.dislikeCount AS dislikeCount,
                   r.spoiler AS spoiler,
                   c.id AS contentId,
                   c.contentType AS contentType,
                   c.title AS contentTitle,
                   c.posterPath AS contentPosterPath,
                   r.createdAt AS createdAt
            FROM Review r
            JOIN r.user u
            JOIN r.content c
            WHERE c.contentType IN :types
            """)
    Page<ReviewFeedView> findMovieAndSeriesReviewFeed(@Param("types") List<ContentType> types, Pageable pageable);

    @Query("""
            SELECT r.id AS id,
                   r.rate AS rate,
                   r.reviewTitle AS title,
                   r.description AS description,
                   r.likeCount AS likeCount,
                   r.dislikeCount AS dislikeCount,
                   r.spoiler AS spoiler,
                   c.id AS contentId,
                   c.contentType AS contentType,
                   c.title AS contentTitle,
                   c.posterPath AS contentPosterPath,
                   r.createdAt AS createdAt
            FROM Review r
            JOIN r.user u
            JOIN r.content c
            WHERE u.username = :username
            ORDER BY r.createdAt DESC
            """)
    Page<UserReviewView> findByUsername(@Param("username") String username, Pageable pageable);

    boolean existsByUserIdAndContentId(int userId, int contentId);

}
