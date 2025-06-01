package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ContentCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "character_name")
    private String characterName;
    @ManyToOne
    @JoinColumn(name = "crew_member_id")
    private CrewMember cast;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    public ContentCast(String characterName, CrewMember cast, Content content) {
        this.characterName = characterName;
        this.cast = cast;
        this.content = content;
    }
}
