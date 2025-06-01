package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.ContentMetaDataDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ContentRepositoryImpl implements ContentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TMDBApiConfiguration tmdbApiConfiguration;

    @Override
    public List<ContentMetaDataDto> filterContent(List<String> genres, Integer year, Integer rate, ContentType contentType, String language, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Class<? extends Content> entityClass = Content.class;
        if (contentType == ContentType.MOVIE) entityClass = Movie.class;
        else if (contentType == ContentType.SERIES) entityClass = Series.class;
        CriteriaQuery<Content> query = criteriaBuilder.createQuery(Content.class);
        Root<? extends Content> root = query.from(entityClass);
        root.fetch("genres", JoinType.LEFT).fetch("genre", JoinType.LEFT);
        query.distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (contentType != null) {
            if (contentType.equals(ContentType.MOVIE)) {
                predicates.add(root.get("contentType").in(ContentType.MOVIE));
            } else predicates.add(root.get("contentType").in(ContentType.SERIES));
        }
        if (contentType == null) {
            predicates.add(root.get("contentType").in(ContentType.SERIES, ContentType.MOVIE));
        }
        if (genres != null && !genres.isEmpty()) {
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<ContentGenre> subRoot = subquery.from(ContentGenre.class);
            Join<ContentGenre, Genre> subGenreJoin = subRoot.join("genre");
            subquery.select(subRoot.get("content").get("id"))
                    .where(subGenreJoin.get("name").in(genres))
                    .groupBy(subRoot.get("content").get("id"))
                    .having(criteriaBuilder.equal(criteriaBuilder.countDistinct(subGenreJoin.get("name")), genres.size()));
            predicates.add(root.get("id").in(subquery));
        }
        if (language != null && !language.isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("language")), "%" + language.toLowerCase() + "%"));
        }
        if (rate != null && rate > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("imdbRate"), rate));
        }
        if (year != null) {
            Expression<Integer> releaseYear = criteriaBuilder.function("year", Integer.class, root.get("releaseDate"));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(releaseYear, year));
            query.orderBy(criteriaBuilder.asc(releaseYear));
        }
        if (sortBy != null && !sortBy.isBlank()) {
            if (sortBy.equals("mostRecent"))
                query.orderBy(criteriaBuilder.desc(root.get("releaseDate")));
            else if (sortBy.equals("topRated")) query.orderBy(criteriaBuilder.desc(root.get("imdbRate")));
        }
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<? extends Content> results = entityManager.createQuery(query).getResultList();
        return results.stream()
                .map(content -> {
                    String slug = null;
                    if (content instanceof Movie movie) {
                        slug = movie.getSlug();
                    } else if (content instanceof Series series) {
                        slug = series.getSlug();
                    }
                    return new ContentMetaDataDto(
                            content.getId(),
                            content.getTitle(),
                            slug,
                            tmdbApiConfiguration.getBaseImageUrl() + content.getPosterPath(),
                            content.getReleaseDate(),
                            content.getImdbRate(),
                            content.getOverview(),
                            content.getGenres().stream()
                                    .map(g -> g.getGenre().getName())
                                    .collect(Collectors.toSet())
                    );
                })
                .toList();
    }

    @Override
    public List<ContentMetaDataDto> searchContent(String keyword) {
        List<ContentMetaDataDto> movies = queryContentMetaData(Movie.class, keyword);
        List<ContentMetaDataDto> series = queryContentMetaData(Series.class, keyword);
        List<ContentMetaDataDto> combined = new ArrayList<>();
        combined.addAll(movies);
        combined.addAll(series);
        return combined.stream().sorted(Comparator.comparing(ContentMetaDataDto::getRate).reversed())
                .toList();
    }

    private <T extends Content> List<ContentMetaDataDto> queryContentMetaData(Class<T> entityClass, String keyword) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Content> query = cb.createQuery(Content.class);
        Root<T> root = query.from(entityClass);
        root.fetch("genres", JoinType.LEFT).fetch("genre", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"));
        }
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.distinct(true);
        List<Content> results = entityManager.createQuery(query).getResultList();
        return results.stream()
                .map(content -> {
                    String slug = null;
                    if (content instanceof Movie movie) {
                        slug = movie.getSlug();
                    } else if (content instanceof Series series) {
                        slug = series.getSlug();
                    }
                    return new ContentMetaDataDto(
                            content.getId(),
                            content.getTitle(),
                            slug,
                            tmdbApiConfiguration.getBaseImageUrl() + content.getPosterPath(),
                            content.getReleaseDate(),
                            content.getImdbRate(),
                            content.getOverview(),
                            content.getGenres().stream()
                                    .map(g -> g.getGenre().getName())
                                    .collect(Collectors.toSet())
                    );
                })
                .toList();
    }
}
