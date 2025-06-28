package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentCast;
import com.cineverse.cineverse.domain.entity.CrewMember;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query("""
            SELECT DISTINCT m FROM Movie m
            JOIN FETCH m.genres cg
            JOIN FETCH cg.genre
            WHERE m.slug = :slug
            """)
    Movie findMovieWithGenres(@Param("slug") String slug);

    @Query("""
            SELECT m FROM Movie m
            JOIN FETCH m.genres cg
            JOIN FETCH cg.genre
            WHERE m.id = :id
            """)
    Movie findMovieById(@Param("id") int id);


    @Query("""
            SELECT d FROM Movie m
            JOIN m.director d
            WHERE m.id = :id
            """)
    CrewMember findMovieDirector(@Param("id") int id);

    @Query("""
            SELECT mc FROM Movie m
            JOIN m.contentCasts mc
            JOIN FETCH mc.cast c
            WHERE m.id = :id
            ORDER BY mc.id
            """)
    List<ContentCast> findMovieCast(@Param("id") int id);

    @Query("""
            SELECT p FROM Movie m
            JOIN m.providers mp
            JOIN mp.provider p
            WHERE m.id = :id
            """)
    List<Provider> findMovieProviders(@Param("id") int id);

    @Query("""
            SELECT DISTINCT m FROM Movie m
            JOIN FETCH m.genres cg
            JOIN FETCH cg.genre
            WHERE m.director.id = :directorId
            """)
    List<Movie> findByDirectorId(int directorId);

    boolean existsBySlug(String slug);

}
