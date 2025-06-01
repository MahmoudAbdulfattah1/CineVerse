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
public class Series extends Content {
    @Column(name = "number_of_seasons")
    private int numberOfSeasons;
    @Column(name = "number_of_episodes")
    private int numberOfEpisodes;
    private String status;
    @Column(name = "production_country")
    private String productionCountry;
    @Column(unique = true)
    private String slug;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private CrewMember director;
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Season> seasons;

    public Series(int tmdbId, String title, String overview, LocalDate releaseDate, String posterPath, String language, float imdbRate, int numberOfSeasons, int numberOfEpisodes, String status, String productionCountry, CrewMember director) {
        super(tmdbId, title, overview, releaseDate, posterPath, language, imdbRate, ContentType.SERIES);
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
        this.status = status;
        this.productionCountry = productionCountry;
        this.director = director;
    }
}
