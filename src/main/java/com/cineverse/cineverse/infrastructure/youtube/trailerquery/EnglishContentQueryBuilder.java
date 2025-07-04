package com.cineverse.cineverse.infrastructure.youtube.trailerquery;

public class EnglishContentQueryBuilder implements TrailerQueryBuilder {

    @Override
    public String build(String title, int releaseYear) {
        StringBuilder query = new StringBuilder(title).append(" ");
        return query.append("official trailer ").append(releaseYear).toString();
    }
}
