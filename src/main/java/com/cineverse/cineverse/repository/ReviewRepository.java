package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Review;
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
                LEFT JOIN FETCH r.reactions react
                WHERE r.content.id = :id
                ORDER BY r.createdAt DESC
            """)
    List<Review> findContentReviews(@Param("id") int id);
}
