package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.domain.entity.Series;
import com.cineverse.cineverse.domain.enums.ContentStatus;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.*;
import com.cineverse.cineverse.dto.content.*;
import com.cineverse.cineverse.dto.crew.CastAndCrewDto;
import com.cineverse.cineverse.mapper.content.*;
import com.cineverse.cineverse.mapper.crew.CrewMemberMapper;
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
    private CrewMemberMapper crewMemberMapper;
    private ContentMapper contentMapper;

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> filterContent(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) ContentType type,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) ContentStatus status,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size) {
        Page<ContentMetaDataDto> filteredContent =
                contentMapper.toContentMetaDataDto(contentService.filterContent(
                        genres, year, rate, type, lang, sortBy, status, order, page, size)
                );
        return ResponseEntity.ok(
                ApiResponse.success(filteredContent, "Filtered content fetched successfully")
        );
    }

    @GetMapping("/filter/options")
    public ResponseEntity<ApiResponse> filterOptions() {
        List<FilterSection> filterSections = filterService.getFilterOptions();
        return ResponseEntity.ok(
                ApiResponse.success(filterSections, "Filter options fetched successfully")
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchByTitle(
            @RequestParam(required = true) String q,
            @RequestParam(required = false) ContentType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Page<ContentMetaDataDto> searchedContent =
                contentMapper.toContentMetaDataDto(contentService.searchContent(q, type,
                        page, size));
        return ResponseEntity.ok(ApiResponse.success(searchedContent, "Search results fetched successfully"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse> getContentDetails(@PathVariable String slug) {
        Content content = contentService.getContentDetails(slug);
        ContentDetailsDto contentDto = (content instanceof Movie movie) ?
                contentMapper.toMovieDto(movie) :
                (content instanceof Series series) ?
                        contentMapper.toSeriesDto(series) :
                        null;
        return ResponseEntity.ok(
                ApiResponse.success(contentDto, "Content details fetched successfully")
        );
    }

    @GetMapping("/{id}/providers")
    public ResponseEntity<ApiResponse> getContentProviders(@PathVariable int id) {
//        List<ProviderDto> providers = providerMapper.map(contentService.getContentProviders(id));
        List<ProviderDto> providers = contentMapper.toProviderDto(contentService.getContentProviders(id));
        return ResponseEntity.ok(
                ApiResponse.success(providers, "Content providers fetched successfully")
        );
    }

    @GetMapping("/{id}/trailer")
    public ResponseEntity<ApiResponse> getContentTrailer(@PathVariable int id) {
        TrailerDto trailerUrl = new TrailerDto(contentService.getContentTrailer(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(trailerUrl, "Content trailer fetched successfully"));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse> getContentStats(@PathVariable int id) {
        ContentStatsDto stats = new ContentStatsDto(
                contentService.getContentTotalReviewsCount(id),
                contentService.getContentTotalWatchlistCount(id),
                contentService.getPlatformRate(id)
        );
        return ResponseEntity.ok(
                ApiResponse.success(stats, "Content stats fetched successfully")
        );
    }

    @GetMapping("/{id}/credits")
    public ResponseEntity<ApiResponse> getContentCredits(@PathVariable int id) {
        CastAndCrewDto credits = new CastAndCrewDto(
                crewMemberMapper.toDirectorDto(contentService.getContentDirector(id)),
                crewMemberMapper.toContentCastDto(contentService.getContentCast(id))
        );
        return ResponseEntity.ok(
                ApiResponse.success(credits, "Content credits fetched successfully")
        );
    }

    @GetMapping("/{id}/seasons")
    public ResponseEntity<ApiResponse> getSeriesSeasons(@PathVariable int id) {
        List<SeasonDto> seasons = contentService.getSeasonsBySeriesId(id).stream()
                .map(contentMapper::toSeasonDto)
                .toList();
        return ResponseEntity.ok(
                ApiResponse.success(seasons, "Series seasons fetched successfully")
        );
    }

    @GetMapping("/{id}/seasons/{number}")
    public ResponseEntity<ApiResponse> getSeriesSeason(@PathVariable int id, @PathVariable int number) {
        SeasonDto season = contentMapper.toSeasonDto(contentService.getSeasonByNumberAndSeriesId(number, id));
        return ResponseEntity.ok(
                ApiResponse.success(season, "Series season fetched successfully")
        );
    }

    @GetMapping("/{id}/seasons/{number}/episodes")
    public ResponseEntity<ApiResponse> getSeasonEpisodes(@PathVariable int id, @PathVariable int number) {
        List<EpisodeDto> episodes =
                contentService.getSeasonEpisodes(number, id).stream()
                        .map(contentMapper::toEpisodeDto)
                        .toList();
        return ResponseEntity.ok(
                ApiResponse.success(episodes, "Season episodes fetched successfully")
        );
    }

    @GetMapping("/{id}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    public ResponseEntity<ApiResponse> getSeasonEpisodes(@PathVariable int id, @PathVariable int seasonNumber,
                                                         @PathVariable int episodeNumber) {
        EpisodeDto episode = contentMapper.toEpisodeDto(
                contentService.getEpisodeByNumberAndSeasonNumberAndSeriesId(id, seasonNumber, episodeNumber));
        return ResponseEntity.ok(
                ApiResponse.success(episode, "Episode details fetched successfully")
        );
    }

    @GetMapping("{contentId}/summary")
    public ResponseEntity<ApiResponse> getContentSummary(
            @PathVariable int contentId,
            @RequestParam ContentType contentType) {
        Content content = contentService.getTypedContentById(contentId, contentType);
        ContentSummaryDto summaryDto = contentMapper.toContentSummary(content);
        return ResponseEntity.ok(ApiResponse.success(summaryDto, "Content summary fetched successfully"));
    }


}
