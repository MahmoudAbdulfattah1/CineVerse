package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String name;
    @Column(name = "profile_path")
    private String profilePath;
    @OneToMany(mappedBy = "cast")
    private Set<ContentCast> contentCasts;
    @OneToMany(mappedBy = "director")
    private Set<Series> series;
    @OneToMany(mappedBy = "director")
    private Set<Movie> movies;

}
