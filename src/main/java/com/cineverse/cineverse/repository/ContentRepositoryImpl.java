package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ContentRepositoryImpl implements ContentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Content> filterContent(List<String> genres, Integer year, Integer rate, ContentType contentType,
                                       String language, String sortBy, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Class<? extends Content> entityClass = Content.class;
        if (contentType == ContentType.MOVIE) {
            entityClass = Movie.class;
        } else if (contentType == ContentType.SERIES) {
            entityClass = Series.class;
        }

        long totalCount = getTotalCount(criteriaBuilder, entityClass, genres, year, rate, contentType, language);

        if (totalCount == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        CriteriaQuery<Integer> idQuery = criteriaBuilder.createQuery(Integer.class);
        Root<? extends Content> idRoot = idQuery.from(entityClass);
        idQuery.select(idRoot.get("id"));
        List<Predicate> idPredicates = buildPredicates(criteriaBuilder, idRoot, idQuery, genres, year, rate,
                contentType, language);

        if (sortBy != null && !sortBy.isBlank()) {
            if (sortBy.equals("mostRecent")) {
                idQuery.orderBy(criteriaBuilder.desc(idRoot.get("releaseDate")));
            } else if (sortBy.equals("topRated")) {
                idQuery.orderBy(criteriaBuilder.desc(idRoot.get("imdbRate")));
            } else {
                idQuery.orderBy(criteriaBuilder.asc(idRoot.get("releaseDate"))); // default
            }
        }

        idQuery.where(criteriaBuilder.and(idPredicates.toArray(new Predicate[0])));

        List<Integer> pageIds = entityManager.createQuery(idQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        if (pageIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, totalCount);
        }

        CriteriaQuery<Content> contentQuery = criteriaBuilder.createQuery(Content.class);
        Root<? extends Content> contentRoot = contentQuery.from(entityClass);
        contentRoot.fetch("genres", JoinType.LEFT).fetch("genre", JoinType.LEFT);
        contentQuery.select((Selection<? extends Content>) contentRoot);
        contentQuery.distinct(true);
        contentQuery.where(contentRoot.get("id").in(pageIds));

        List<? extends Content> contentResults = entityManager.createQuery(contentQuery).getResultList();

        Map<Integer, Content> contentMap = contentResults.stream()
                .collect(Collectors.toMap(Content::getId, content -> content));

        List<Content> orderedContent = pageIds.stream()
                .map(contentMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(orderedContent, pageable, totalCount);
    }

    private long getTotalCount(CriteriaBuilder criteriaBuilder, Class<? extends Content> entityClass,
                               List<String> genres, Integer year, Integer rate, ContentType contentType,
                               String language) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<? extends Content> countRoot = countQuery.from(entityClass);
        countQuery.select(criteriaBuilder.countDistinct(countRoot));

        List<Predicate> countPredicates = buildPredicates(criteriaBuilder, countRoot, countQuery, genres, year, rate,
                contentType, language);
        countQuery.where(criteriaBuilder.and(countPredicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public Page<Content> searchContent(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        int totalNeeded = (int) (pageable.getOffset() + pageable.getPageSize());
        int fetchLimit = Math.max(totalNeeded * 2, 100);

        List<Movie> movies = queryContent(Movie.class, keyword, fetchLimit);
        List<Series> series = queryContent(Series.class, keyword, fetchLimit);

        List<Content> combined = new ArrayList<>();
        combined.addAll(movies);
        combined.addAll(series);

        List<Content> sortedContent = combined.stream()
                .sorted(Comparator.comparing(Content::getImdbRate).reversed())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sortedContent.size());

        List<Content> pageContent = start >= sortedContent.size() ? Collections.emptyList() :
                sortedContent.subList(start, end);


        long totalMovies = countContent(Movie.class, keyword);
        long totalSeries = countContent(Series.class, keyword);
        long total = totalMovies + totalSeries;

        return new PageImpl<>(pageContent, pageable, total);
    }

    private <T extends Content> List<T> queryContent(Class<T> entityClass, String keyword, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<T> idRoot = idQuery.from(entityClass);
        idQuery.select(idRoot.get("id")).distinct(true);

        Predicate predicate = cb.like(cb.lower(idRoot.get("title")), "%" + keyword.toLowerCase() + "%");
        idQuery.where(predicate);

        List<Integer> ids =
                entityManager.createQuery(idQuery).setMaxResults(limit).getResultList();

        if (ids.isEmpty()) return Collections.emptyList();
        CriteriaQuery<T> contentQuery = cb.createQuery(entityClass);
        Root<T> contentRoot = contentQuery.from(entityClass);
        contentRoot.fetch("genres", JoinType.LEFT).fetch("genre", JoinType.LEFT);
        contentQuery.select(contentRoot).distinct(true);
        contentQuery.where(contentRoot.get("id").in(ids));

        return entityManager.createQuery(contentQuery).getResultList();
    }

    private <T extends Content> long countContent(Class<T> entityClass, String keyword) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);
        countQuery.select(cb.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"));
        }
        countQuery.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }


    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<? extends Content> root,
                                            CriteriaQuery<?> query, List<String> genres, Integer year, Integer rate,
                                            ContentType contentType, String language) {
        List<Predicate> predicates = new ArrayList<>();

        if (contentType != null) {
            predicates.add(root.get("contentType").in(contentType));
        } else {
            predicates.add(root.get("contentType").in(ContentType.MOVIE, ContentType.SERIES));
        }

        if (genres != null && !genres.isEmpty()) {
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<ContentGenre> subRoot = subquery.from(ContentGenre.class);
            Join<ContentGenre, Genre> subGenreJoin = subRoot.join("genre");

            subquery.select(subRoot.get("content").get("id")).where(subGenreJoin.get("name").in(genres)).groupBy(subRoot.get("content").get("id")).having(criteriaBuilder.equal(criteriaBuilder.countDistinct(subGenreJoin.get("name")), genres.size()));

            predicates.add(root.get("id").in(subquery));
        }

        if (language != null && !language.isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("language")),
                    "%" + language.toLowerCase() + "%"));
        }

        if (rate != null && rate > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("imdbRate"), rate));
        }

        if (year != null) {
            Expression<String> yearStr = criteriaBuilder.function("to_char", String.class, root.get("releaseDate"),
                    criteriaBuilder.literal("YYYY"));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(yearStr, year.toString()));
        }

        return predicates;
    }


}
