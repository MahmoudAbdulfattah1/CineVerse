package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.*;
import com.cineverse.cineverse.service.ContentService;
import com.cineverse.cineverse.service.FilterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contents")
@AllArgsConstructor
public class ContentController {
    private ContentService contentService;
    private FilterService filterService;

    @GetMapping("/filter")
    public ResponseEntity<List<ContentMetaDataDto>> filterContent(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) ContentType type,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(contentService.filterContent(genres, year, rate, type, lang, sortBy));
    }

    @GetMapping("/filter/options")
    public ResponseEntity<List<FilterSection>> filterOptions() {
        return ResponseEntity.ok(filterService.getFilterOptions());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContentMetaDataDto>> searchByTitle(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(contentService.searchContent(q));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ContentDetailsDto> getContentDetails(@PathVariable String slug) {
        return ResponseEntity.ok(contentService.getContentDetails(slug));
    }

    @GetMapping("/{id}/providers")
    public ResponseEntity<List<ProviderDto>> getContentProviders(@PathVariable int id) {
        return ResponseEntity.ok(contentService.getContentProviders(id));
    }

    @GetMapping("/{id}/trailer")
    public ResponseEntity<TrailerDto> getContentTrailer(@PathVariable int id) {
        return ResponseEntity.ok(contentService.getContentTrailer(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ContentStatsDto> getContentStats(@PathVariable int id) {
        return ResponseEntity.ok(contentService.getContentStats(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getContentReviews(@PathVariable int id) {
        return ResponseEntity.ok(contentService.getContentReviews(id));
    }

    @GetMapping("/{id}/credits")
    public ResponseEntity<CastAndCrewDto> getContentCredits(@PathVariable int id) {
        return ResponseEntity.ok(contentService.getContentCredits(id));
    }
}
