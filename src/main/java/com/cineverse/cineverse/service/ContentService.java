package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentStatus;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.content.UnsupportedContentTypeException;
import com.cineverse.cineverse.infrastructure.youtube.YoutubeClient;
import com.cineverse.cineverse.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ContentService {
    private ContentRepository contentRepository;
    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;
    private SeasonRepository seasonRepository;
    private EpisodeRepository episodeRepository;
    private ContentTypeRepository contentTypeRepository;
    private ContentTrailerRepository contentTrailerRepository;
    private YoutubeClient youtubeClient;

    public Page<Content> filterContent(List<String> genres, Integer year, Integer rate,
                                       ContentType contentType, String language, String sortBy, ContentStatus status,
                                       String order, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contentRepository.filterContent(genres, year, rate, contentType, language, sortBy, status, order,
                pageable);
    }

    public Page<Content> searchContent(String keyword, ContentType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contentRepository.searchContent(keyword, type, pageable);
    }

    public Movie getMovieDetails(String movieSlug) {
        return movieRepository.findMovieWithGenres(movieSlug);
    }

    public Series getSeriesDetails(String slug) {
        return seriesRepository.findSeriesWithGenres(slug);
    }

    public Content getContentDetails(String slug) {
        if (movieRepository.existsBySlug(slug)) {
            return getMovieDetails(slug);
        } else if (seriesRepository.existsBySlug(slug)) {
            return getSeriesDetails(slug);
        }
        throw new ContentNotFoundException("Content is not found");
    }


    public String getContentTrailer(int contentId) {
        String trailerId = contentTrailerRepository.findYoutubeIdByContentId(contentId);
        if (trailerId != null) {
            return trailerId;
        }
        ContentType contentType = contentTypeRepository.findContentTypeById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("Content not found"));
        Content content = switch (contentType) {
            case MOVIE -> movieRepository.findMovieById(contentId);
            case SERIES -> seriesRepository.findSeriesById(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type: " + contentType);
        };
        return youtubeClient.getTrailerUrl(
                content.getTitle(),
                content.getReleaseDate().getYear(),
                content.getContentType(),
                content.getLanguage(),
                content.getGenres().stream()
                        .map(genre -> genre.getGenre().getName())
                        .toList()
        );
    }

    public float getPlatformRate(int contentId) {
        float rate = contentRepository.getPlatformRate(contentId);
        return Math.round(rate * 10) / 10.0f;
    }

    public int getContentTotalReviewsCount(int contentId) {
        return contentRepository.totalReviewsCount(contentId);
    }

    public int getContentTotalWatchlistCount(int contentId) {
        return contentRepository.totalWatchlistCount(contentId);
    }

    public CrewMember getContentDirector(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieDirector(contentId);
            case SERIES -> seriesRepository.findSeriesDirector(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    public List<ContentCast> getContentCast(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieCast(contentId);
            case SERIES -> seriesRepository.findSeriesCast(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    public List<Provider> getContentProviders(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieProviders(contentId);
            case SERIES -> seriesRepository.findSeriesProviders(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    public List<Season> getSeasonsBySeriesId(int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return seasonRepository.findAllSeasonsBySeriesId(seriesId);
    }

    public Season getSeasonByNumberAndSeriesId(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return seasonRepository.findSeasonBySeasonNumberAndSeriesId(seasonNumber, seriesId);
    }

    public List<Episode> getSeasonEpisodes(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return episodeRepository.findAllEpisodesBySeriesIdAndSeasonNumber(seriesId, seasonNumber);
    }

    public Episode getEpisodeByNumberAndSeasonNumberAndSeriesId(int seriesId, int seasonNumber, int episodeNumber) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return episodeRepository.findEpisodeByNumberAndSeasonNumberAndSeriesId(seriesId, seasonNumber, episodeNumber);
    }

    public int getEpisodeCountBySeasonId(int seasonId) {
        return episodeRepository.countBySeasonId(seasonId);
    }

    private ContentType getContentTypeOrThrow(int contentId) {
        return contentTypeRepository.findContentTypeById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("Content not found"));
    }

    public Content getTypedContentById(int contentId, ContentType contentType) {
        return switch (contentType) {
            case MOVIE -> movieRepository.findById(contentId)
                    .orElseThrow(() -> new ContentNotFoundException("Movie not found"));
            case SERIES -> seriesRepository.findById(contentId)
                    .orElseThrow(() -> new ContentNotFoundException("Series not found"));
            case SEASON -> seasonRepository.findById(contentId)
                    .orElseThrow(() -> new ContentNotFoundException("Season not found"));
            case EPISODE -> episodeRepository.findById(contentId)
                    .orElseThrow(() -> new ContentNotFoundException("Episode not found"));
        };
    }

}
