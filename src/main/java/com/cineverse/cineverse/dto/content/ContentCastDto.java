package com.cineverse.cineverse.dto.content;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContentCastDto {

    private int id;
    private String characterName;
    private String name;
    private String imageUrl;

    public ContentCastDto(int id, String characterName, String name, String imageUrl) {
        this.id = id;
        this.characterName = characterName;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
