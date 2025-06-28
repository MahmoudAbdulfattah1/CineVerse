package com.cineverse.cineverse.domain.trailerquery;

public class ArabicMovieQueryBuilder implements TrailerQueryBuilder {
    @Override
    public String build(String title, int releaseYear) {
        return "فيلم " + title + " " + releaseYear + " الاعلان الرسمي";
    }
}
