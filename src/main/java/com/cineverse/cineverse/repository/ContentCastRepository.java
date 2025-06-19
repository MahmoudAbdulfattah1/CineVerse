package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentCast;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.domain.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentCastRepository extends JpaRepository<ContentCast, Integer> {
    @Query("""
            SELECT m
            FROM ContentCast cc
            JOIN Movie m
            ON cc.content = m
            JOIN FETCH m.genres mg
            JOIN FETCH mg.genre
            WHERE cc.cast.id = :castId
             """)
    List<Movie> findMoviesByCastId(@Param("castId") int castId);

    @Query("""
            SELECT s
            FROM ContentCast cc
            JOIN Series s
            ON cc.content = s
            JOIN FETCH s.genres sg
            JOIN FETCH sg.genre
            WHERE cc.cast.id = :castId
             """)
    List<Series> findSeriesByCastId(@Param("castId") int castId);

}
