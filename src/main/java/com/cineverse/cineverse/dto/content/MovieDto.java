package com.cineverse.cineverse.dto.content;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class MovieDto implements ContentDetailsDto {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;
    private int runtime;
    private String language;
    private String productionCountry;
    private float imdbRate;
    private List<String> genres;

    public MovieDto(int id, String title, String overview, String posterPath, String backdropPath,
                    LocalDate releaseDate, int runtime, String language, String productionCountry, float imdbRate,
                    List<String> genres) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.language = language;
        this.productionCountry = productionCountry;
        this.imdbRate = imdbRate;
        this.genres = genres;
    }
}
