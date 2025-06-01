package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.dto.ProviderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>, ContentRepositoryCustom {

    @Query("SELECT COALESCE(COUNT(r), 0) FROM Content c JOIN c.reviews r WHERE c.id = :id")
    int totalReviews(@Param("id") int id);

    @Query("""
            SELECT COALESCE(AVG(r.rate), 0.0)
            FROM Content c
            JOIN c.reviews r
            WHERE c.id = :id
            """)
    float getPlatformRate(@Param("id") int id);

    @Query("SELECT COALESCE(COUNT(w), 0) FROM Content c JOIN c.watchlists w WHERE c.id = :id")
    int totalWatchlists(@Param("id") int id);

    @Query("SELECT c.title FROM Content c WHERE c.id = :id")
    String findContentTitleWithId(@Param("id") int id);

    @Query("""
            SELECT new com.cineverse.cineverse.dto.ProviderDto(p.name, p.logo)
            FROM Content c
            JOIN c.providers mp
            JOIN mp.provider p
            WHERE c.id = :id
            """)
    List<ProviderDto> findContentProviders(@Param("id") int id);
}
