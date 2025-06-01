package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "content_genre")
@Getter
@Setter
@NoArgsConstructor
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

    public ContentGenre(Genre genre, Content content) {
        this.genre = genre;
        this.content = content;
    }
}
