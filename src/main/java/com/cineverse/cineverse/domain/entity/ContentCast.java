package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "content_cast")
@Data
public class ContentCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "character_name")
    private String characterName;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person cast;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
