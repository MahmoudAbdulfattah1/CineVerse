package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "content_provider")
@Data
public class ContentProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
