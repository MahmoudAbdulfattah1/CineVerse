package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id")
    private int tmdbId;
    private String name;
    @OneToMany(mappedBy = "genre")
    private Set<ContentGenre> genres;

}
