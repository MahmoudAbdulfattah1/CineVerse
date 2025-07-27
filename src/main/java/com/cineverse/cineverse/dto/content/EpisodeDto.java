package com.cineverse.cineverse.dto.content;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EpisodeDto {
    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private int episodeNumber;
    private float imdbRate;
    private int runTime;
    private LocalDate releaseDate;
}
