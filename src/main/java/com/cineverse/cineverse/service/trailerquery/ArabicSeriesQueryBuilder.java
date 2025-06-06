package com.cineverse.cineverse.service.trailerquery;

public class ArabicSeriesQueryBuilder implements TrailerQueryBuilder {

    @Override
    public String build(String title, int releaseYear) {
        return "مسلسل " + title + " الاعلان الرسمي";
    }
}
