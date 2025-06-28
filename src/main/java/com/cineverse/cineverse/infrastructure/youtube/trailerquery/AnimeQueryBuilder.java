package com.cineverse.cineverse.infrastructure.youtube.trailerquery;

public class AnimeQueryBuilder implements TrailerQueryBuilder {
    @Override
    public String build(String title, int releaseYear) {
        return title + " anime " + releaseYear + " official trailer";
    }


}
