package com.cineverse.cineverse.dto.crew;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectorDto {
    private int id;
    private String name;
    private String imageUrl;


    public DirectorDto(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }


}
