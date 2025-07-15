package com.cineverse.cineverse.dto.content;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SeasonDto {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private int seasonNumber;
    private float imdbRate;
    private LocalDate releaseDate;
    private int numberOfEpisodes;

    public SeasonDto(int id, String title, String overview, String posterPath, int seasonNumber, float imdbRate,
                     LocalDate releaseDate, int numberOfEpisodes) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
        this.imdbRate = imdbRate;
        this.releaseDate = releaseDate;
        this.numberOfEpisodes = numberOfEpisodes;
    }
}
