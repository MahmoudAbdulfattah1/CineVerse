package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.*;
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
            SELECT  s FROM Series s
            JOIN FETCH s.genres cg
            JOIN FETCH cg.genre
            WHERE s.id = :id
            """)
    Series findSeriesById(int id);

    @Query("""
            SELECT d FROM Series s
            JOIN s.director d
            WHERE s.id = :id
            """)
    CrewMember findSeriesDirector(@Param("id") int id);

    @Query("""
            SELECT sc FROM Series s
            JOIN s.contentCasts sc
            JOIN FETCH sc.cast c
            WHERE s.id = :id
            ORDER BY sc.id
            """)
    List<ContentCast> findSeriesCast(@Param("id") int id);

    @Query("""
            SELECT p FROM Series s
            JOIN s.providers sp
            JOIN sp.provider p
            WHERE s.id = :id
            """)
    List<Provider> findSeriesProviders(@Param("id") int id);

    @Query("""
            SELECT DISTINCT s FROM Series s
            JOIN FETCH s.genres cg
            JOIN FETCH cg.genre
            WHERE s.director.id = :directorId
            """)
    List<Series> findByDirectorId(int directorId);

    boolean existsBySlug(String slug);

    boolean existsById(int id);


}
