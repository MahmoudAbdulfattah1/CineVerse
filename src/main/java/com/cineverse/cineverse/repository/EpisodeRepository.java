package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    @Query("""
            SELECT e FROM Episode e
            WHERE e.season.series.id = :seriesId AND e.season.seasonNumber = :seasonNumber
            """)
    List<Episode> findAllEpisodesBySeriesIdAndSeasonNumber(int seriesId, int seasonNumber);

    @Query("""
            SELECT e FROM Episode e
            WHERE e.season.series.id = :seriesId AND e.season.seasonNumber = :seasonNumber And e.episodeNumber = :episodeNumber
            """)
    Episode findEpisodeByNumberAndSeasonNumberAndSeriesId(int seriesId, int seasonNumber, int episodeNumber);

    @Query("""
            SELECT COUNT(e) FROM Episode e
            WHERE e.season.id = :seasonId
            """)
    int countBySeasonId(int seasonId);


}
