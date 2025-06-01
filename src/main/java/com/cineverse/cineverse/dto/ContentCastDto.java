package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContentCastDto {

    private int id;
    private String characterName;
    private String name;
    private String path;

    public ContentCastDto(int id, String characterName, String name, String path) {
        this.id = id;
        this.characterName = characterName;
        this.name = name;
        this.path = path;
    }
}
