package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.GenreType;
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
    @Enumerated(EnumType.STRING)
    private GenreType type;
    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ContentGenre> genres;

    public Genre(int tmdbId, String name, GenreType type) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.type = type;
    }
}
