package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ContentMetaDataDto {
    private int id;
    private String title;
    private String slug;
    private String posterPath;
    private LocalDate releaseDate;
    private Float rate;
    private String overview;
    private  String type;
    private Set<String> genres;

    public ContentMetaDataDto(int id, String title, String slug, String posterPath, LocalDate releaseDate, Float rate, String overview, String type, Set<String> genres) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.rate = rate;
        this.overview = overview;
        this.type = type;
        this.genres = genres;
    }

}
