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
public class Movie extends Content {
    @Column(name = "run_time")
    private int runtime;
    @Column(name = "production_country")
    private String productionCountry;
    @Column(unique = true)
    private String slug;
    private String backdropPath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private CrewMember director;

    public Movie(int tmdbId, String title, String overview, LocalDate releaseDate, String posterPath, String language, float imdbRate, String backdropPath, int runtime, String productionCountry, CrewMember director) {
        super(tmdbId, title, overview, releaseDate, posterPath, language, imdbRate, ContentType.MOVIE);
        this.backdropPath = backdropPath;
        this.runtime = runtime;
        this.productionCountry = productionCountry;
        this.director = director;
    }
}
