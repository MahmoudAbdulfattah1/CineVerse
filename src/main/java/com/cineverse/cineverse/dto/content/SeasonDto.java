package com.cineverse.cineverse.dto.content;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonDto {
    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private int seasonNumber;
    private float imdbRate;
    private LocalDate releaseDate;
    private int numberOfEpisodes;
}
