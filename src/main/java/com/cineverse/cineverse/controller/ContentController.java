package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.domain.entity.Series;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.*;
import com.cineverse.cineverse.mapper.*;
import com.cineverse.cineverse.service.ContentService;
import com.cineverse.cineverse.service.FilterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contents")
@AllArgsConstructor
public class ContentController {
    private ContentService contentService;
    private FilterService filterService;
    private ProviderMapper providerMapper;
    private CreditMapper creditMapper;
    private ReviewMapper reviewMapper;
    private MovieMapper movieMapper;
    private SeriesMapper seriesMapper;
    private ContentMetaDataMapper contentMetaDataMapper;

    @GetMapping("/filter")
    public ResponseEntity<Page<ContentMetaDataDto>> filterContent(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) ContentType type,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(contentMetaDataMapper.toDto(
                contentService.filterContent(genres, year, rate, type, lang, sortBy, page, size))
        );
    }

    @GetMapping("/filter/options")
    public ResponseEntity<List<FilterSection>> filterOptions() {
        return ResponseEntity.ok(filterService.getFilterOptions());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ContentMetaDataDto>> searchByTitle(
            @RequestParam(required = true) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(contentMetaDataMapper.toDto(contentService.searchContent(q, page, size)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ContentDetailsDto> getContentDetails(@PathVariable String slug) {
        Content content = contentService.getContentDetails(slug);
        ContentDetailsDto dto = (content instanceof Movie movie) ?
                movieMapper.toDto(movie) :
                (content instanceof Series series) ?
                        seriesMapper.toDto(series) :
                        null;
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/providers")
    public ResponseEntity<List<ProviderDto>> getContentProviders(@PathVariable int id) {
        return ResponseEntity.ok(providerMapper.map(contentService.getContentProviders(id)));
    }

    @GetMapping("/{id}/trailer")
    public ResponseEntity<TrailerDto> getContentTrailer(@PathVariable int id) {
        return ResponseEntity.ok(new TrailerDto(contentService.getContentTrailer(id)));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ContentStatsDto> getContentStats(@PathVariable int id) {
        return ResponseEntity.ok(new ContentStatsDto(
                contentService.getContentTotalReviewsCount(id),
                contentService.getContentTotalWatchlistCount(id))
        );
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getContentReviews(@PathVariable int id) {
        return ResponseEntity.ok(reviewMapper.toDto(contentService.getReviews(id)));
    }

    @GetMapping("/{id}/credits")
    public ResponseEntity<CastAndCrewDto> getContentCredits(@PathVariable int id) {
        return ResponseEntity.ok(new CastAndCrewDto(
                creditMapper.mapDirector(contentService.getContentDirector(id)),
                creditMapper.mapContentCast(contentService.getContentCast(id)))
        );
    }
}
