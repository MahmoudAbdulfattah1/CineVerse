package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id")
    private int tmdbId;
    private String name;
    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private Set<ContentGenre> genres;
    public Genre(int tmdbId, String name) {
        this.tmdbId = tmdbId;
        this.name = name;
    }
}
