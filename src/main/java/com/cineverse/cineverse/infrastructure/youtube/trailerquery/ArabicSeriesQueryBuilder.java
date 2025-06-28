package com.cineverse.cineverse.infrastructure.youtube.trailerquery;

public class ArabicSeriesQueryBuilder implements TrailerQueryBuilder {

    @Override
    public String build(String title, int releaseYear) {
        return "مسلسل " + title + " " + releaseYear + " الاعلان الرسمي";
    }
}
