package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentTrailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentTrailerRepository extends JpaRepository<ContentTrailer, Integer> {

    @Query("""
            SELECT youtubeId FROM ContentTrailer ct
            WHERE ct.content.id = :contentId
            """)
    String findYoutubeIdByContentId(@Param("contentId") int contentId);
}
