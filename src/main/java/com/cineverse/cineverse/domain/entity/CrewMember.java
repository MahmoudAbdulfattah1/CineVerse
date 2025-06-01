package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CrewMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String name;
    @Column(name = "profile_path")
    private String profilePath;
    @OneToMany(mappedBy = "cast", fetch = FetchType.LAZY)
    private Set<ContentCast> contentCasts;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private Set<Series> series;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private Set<Movie> movies;

    public CrewMember(int tmdbId, String name, String profilePath) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.profilePath = profilePath;
    }
}
