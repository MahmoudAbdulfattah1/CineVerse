package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Season extends Content {

    @Column(name = "season_number")
    private int seasonNumber;
    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Episode> episodes;

    public Season(int tmdbId, String title, String overview, LocalDate releaseDate, String posterPath, String language, float imdbRate,  int seasonNumber, Series series) {
        super(tmdbId, title, overview, releaseDate, posterPath, language, imdbRate, ContentType.SEASON);
        this.seasonNumber = seasonNumber;
        this.series = series;
    }
}
