package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Episode extends Content {
    @Column(name = "run_time")
    private int runTime;
    private int episodeNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    public Episode(int tmdbId, String title, String overview, LocalDate releaseDate, String posterPath, String language, float imdbRate, int runTime, int episodeNumber, Season season) {
        super(tmdbId, title, overview, releaseDate, posterPath, language, imdbRate, ContentType.EPISODE);
        this.runTime = runTime;
        this.episodeNumber = episodeNumber;
        this.season = season;
    }
}
