package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String title;
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
    @OneToMany(mappedBy = "content")
    private Set<ContentCast> contentCasts;
    @OneToMany(mappedBy = "content")
    private Set<ContentProvider> providers;
    @OneToMany(mappedBy = "content")
    private Set<ContentGenre> genres;
    @OneToMany(mappedBy = "content")
    private Set<Review> reviews;
    @OneToMany(mappedBy = "content")
    private Set<Watchlist> watchlists;


}
