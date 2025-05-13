package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String name;
    private String logo;
    @OneToMany(mappedBy = "provider")
    private Set<ContentProvider> contents;
}
