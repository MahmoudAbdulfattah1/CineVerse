package com.cineverse.cineverse.dto;

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
    private float rate;
    private LocalDate releaseDate;
    private int numberOfEpisodes;

    public SeasonDto(int id, String title, String overview, String posterPath, int seasonNumber, float rate,
                     LocalDate releaseDate, int numberOfEpisodes) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
        this.rate = rate;
        this.releaseDate = releaseDate;
        this.numberOfEpisodes = numberOfEpisodes;
    }
}
