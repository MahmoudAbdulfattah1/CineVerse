package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String name;
    private String logo;
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private Set<ContentProvider> contents;

    public Provider(int tmdbId, String name, String logo) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.logo = logo;
    }
}
