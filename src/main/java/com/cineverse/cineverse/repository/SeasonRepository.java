package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {
    @Query("""
            SELECT s FROM Season s
            WHERE s.series.id = :seriesId
            """)
    List<Season> findAllSeasonsBySeriesId(int seriesId);
    @Query("""
            SELECT s FROM Season s
            WHERE s.series.id = :seriesId AND s.seasonNumber = :seasonNumber
            """)
    Season findSeasonBySeasonNumberAndSeriesId(int seasonNumber, int seriesId);

}
