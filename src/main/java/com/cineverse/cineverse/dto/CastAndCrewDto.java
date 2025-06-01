package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CastAndCrewDto {
    private DirectorDto director;
    private List<ContentCastDto> casts;

    public CastAndCrewDto(DirectorDto director, List<ContentCastDto> casts) {
        this.director = director;
        this.casts = casts;
    }
}
