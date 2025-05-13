package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "content_genre")
public class ContentGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
