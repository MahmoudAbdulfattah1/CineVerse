package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EpisodeDto {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private int episodeNumber;
    private float imdbRate;
    private int runTime;
    private LocalDate releaseDate;

    public EpisodeDto(int id, String title, String overview, String posterPath, int episodeNumber, float imdbRate,
                      int runTime, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.episodeNumber = episodeNumber;
        this.imdbRate = imdbRate;
        this.runTime = runTime;
        this.releaseDate = releaseDate;
    }
}
