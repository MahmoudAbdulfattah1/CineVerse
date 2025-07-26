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
    private String posterUrl;
    private int seasonNumber;
    private float imdbRate;
    private LocalDate releaseDate;
    private int numberOfEpisodes;

    public SeasonDto(int id, String title, String overview, String posterUrl, int seasonNumber, float imdbRate,
                     LocalDate releaseDate, int numberOfEpisodes) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.seasonNumber = seasonNumber;
        this.imdbRate = imdbRate;
        this.releaseDate = releaseDate;
        this.numberOfEpisodes = numberOfEpisodes;
    }
}
