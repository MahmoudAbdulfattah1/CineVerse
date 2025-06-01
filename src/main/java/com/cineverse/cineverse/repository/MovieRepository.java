package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Movie;

import com.cineverse.cineverse.dto.ContentCastDto;
import com.cineverse.cineverse.dto.DirectorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("""
            SELECT DISTINCT m FROM Movie m
            LEFT JOIN FETCH m.genres cg
            LEFT JOIN FETCH cg.genre
            """)
    List<Movie> findAllWithGenres();

    @Query("""
            SELECT DISTINCT m FROM Movie m
            JOIN FETCH m.genres cg
            JOIN FETCH cg.genre
            WHERE m.slug = :slug
            """)
    Movie findMovieWithGenres(@Param("slug") String slug);


    @Query("""
            SELECT new com.cineverse.cineverse.dto.DirectorDto(d.id, d.name, d.profilePath)
            FROM Movie m
            JOIN m.director d
            WHERE m.id = :id
            """)
    DirectorDto findMovieDirector(@Param("id") int id);

    @Query("""
            SELECT new com.cineverse.cineverse.dto.ContentCastDto(c.id, mc.characterName, c.name, c.profilePath)
            FROM Movie m
            JOIN m.contentCasts mc
            JOIN mc.cast c
            WHERE m.id = :id
            ORDER BY mc.id
            """)
    List<ContentCastDto> findMovieCasts(@Param("id") int id);

    boolean existsBySlug(String slug);

}
