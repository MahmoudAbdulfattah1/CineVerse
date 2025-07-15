package com.cineverse.cineverse.dto.crew;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectorDto {
    private int id;
    private String name;
    private String path;


    public DirectorDto(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }


}
