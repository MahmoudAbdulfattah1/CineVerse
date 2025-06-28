package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id")
    private int tmdbId;
    private String title;
    @Column(length = 3500)
    private String overview;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "poster_path")
    private String posterPath;
    @Column(name = "original_language")
    private String language;
    @Column(name = "imdb_rate")
    private float imdbRate;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private Set<ContentCast> contentCasts;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<ContentProvider> providers;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<ContentGenre> genres;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private Set<Watchlist> watchlists;


    public Content(int tmdbId, String title, String overview, LocalDate releaseDate, String posterPath, String language, float imdbRate, ContentType contentType) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.language = language;
        this.imdbRate = imdbRate;
        this.contentType = contentType;
    }
}
