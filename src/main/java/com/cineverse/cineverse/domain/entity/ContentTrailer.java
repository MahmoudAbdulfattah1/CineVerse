package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ContentTrailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "youtube_id", nullable = false)
    private String youtubeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", unique = true)
    private Content content;
    public ContentTrailer(String youtubeId, Content content) {
        this.youtubeId = youtubeId;
        this.content = content;
    }

}
