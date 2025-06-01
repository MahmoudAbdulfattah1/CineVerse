package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_provider")
@Getter
@Setter
@NoArgsConstructor
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

    public ContentProvider(Provider provider, Content content) {
        this.provider = provider;
        this.content = content;
    }
}
