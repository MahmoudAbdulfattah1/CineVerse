package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>, ContentRepositoryCustom {

    @Query("SELECT COALESCE(COUNT(r), 0) FROM Content c JOIN c.reviews r WHERE c.id = :id")
    int totalReviewsCount(@Param("id") int id);

    @Query("""
            SELECT COALESCE(AVG(r.rate), 0.0)
            FROM Content c
            JOIN c.reviews r
            WHERE c.id = :id
            """)
    float getPlatformRate(@Param("id") int id);

    @Query("SELECT COALESCE(COUNT(w), 0) FROM Content c JOIN c.watchlists w WHERE c.id = :id")
    int totalWatchlistCount(@Param("id") int id);

    @Query("SELECT c FROM Content c WHERE c.id = :id")
    Optional<Content> findContentById(@Param("id") int id);

    boolean existsById(int id);
}
