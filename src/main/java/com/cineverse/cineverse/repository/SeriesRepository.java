package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Series;
import com.cineverse.cineverse.dto.ContentCastDto;
import com.cineverse.cineverse.dto.DirectorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    @Query("""
            SELECT DISTINCT s FROM Series s
            JOIN FETCH s.genres cg
            JOIN FETCH cg.genre
            WHERE s.slug = :slug
            """)
    Series findSeriesWithGenres(@Param("slug") String slug);

    @Query("""
            SELECT new com.cineverse.cineverse.dto.DirectorDto(d.id, d.name, d.profilePath)
            FROM Series s
            JOIN s.director d
            WHERE s.id = :id
            """)
    DirectorDto findSeriesDirector(@Param("id") int id);

    @Query("""
            SELECT new com.cineverse.cineverse.dto.ContentCastDto(c.id, sc.characterName, c.name, c.profilePath)
            FROM Series s
            JOIN s.contentCasts sc
            JOIN sc.cast c
            WHERE s.id = :id
            ORDER BY sc.id
            """)
    List<ContentCastDto> findSeriesCasts(@Param("id") int id);

    boolean existsBySlug(String slug);

}
