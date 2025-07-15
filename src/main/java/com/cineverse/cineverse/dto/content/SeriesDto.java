package com.cineverse.cineverse.dto.content;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SeriesDto implements ContentDetailsDto {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;
    private String language;
    private String productionCountry;
    private float imdbRate;
    private int numberOfSeasons;
    private int numberOfEpisodes;
    private String status;
    private List<String> genres;

    public SeriesDto(int id, String title, String overview, String posterPath, String backdropPath, LocalDate releaseDate, String language, String productionCountry, float imdbRate, int numberOfSeasons, int numberOfEpisodes, String status, List<String> genres) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.language = language;
        this.productionCountry = productionCountry;
        this.imdbRate = imdbRate;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
        this.status = status;
        this.genres = genres;
    }
}
