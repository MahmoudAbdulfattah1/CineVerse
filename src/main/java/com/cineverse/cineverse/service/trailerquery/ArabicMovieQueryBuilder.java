package com.cineverse.cineverse.service.trailerquery;

public class ArabicMovieQueryBuilder implements TrailerQueryBuilder {

    @Override
    public String build(String title, int releaseYear) {
       return "فيلم " + title + " الاعلان الرسمي";
    }
}
