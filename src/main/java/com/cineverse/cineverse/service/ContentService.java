package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    private ReviewRepository reviewRepository;
    private ContentTypeRepository contentTypeRepository;
    private YoutubeService youtubeService;

    public Page<Content> filterContent(List<String> genres, Integer year, Integer rate,
                                       ContentType contentType, String language, String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contentRepository.filterContent(genres, year, rate, contentType, language, sortBy, pageable);
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
        throw new EntityNotFoundException("Content not found");
    }


    public String getContentTrailer(int contentId) {
        ContentType contentType = contentTypeRepository.findContentTypeById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
        Content content = switch (contentType) {
            case MOVIE -> movieRepository.findMovieById(contentId);
            case SERIES -> seriesRepository.findSeriesById(contentId);
            default -> throw new IllegalArgumentException("Unsupported content type: " + contentType);
        };
        return youtubeService.getTrailerUrl(
                content.getTitle(),
                content.getReleaseDate().getYear(),
                content.getContentType()
        );
    }


    public List<Review> getReviews(int contentId) {
        return contentRepository.existsById(contentId) ? reviewRepository.findContentReviews(contentId) : null;
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
            default -> throw new IllegalArgumentException("Unsupported content type.");
        };
    }

    public List<ContentCast> getContentCast(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieCast(contentId);
            case SERIES -> seriesRepository.findSeriesCast(contentId);
            default -> throw new IllegalArgumentException("Unsupported content type.");
        };
    }

    public List<Provider> getContentProviders(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieProviders(contentId);
            case SERIES -> seriesRepository.findSeriesProviders(contentId);
            default -> throw new IllegalArgumentException("Unsupported content type.");
        };
    }

    public List<Season> getSeasonsBySeriesId(int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new EntityNotFoundException("Series not found");
        }
        return seasonRepository.findAllSeasonsBySeriesId(seriesId);
    }

    public Season getSeasonByNumberAndSeriesId(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new EntityNotFoundException("Series not found");
        }
        return seasonRepository.findSeasonBySeasonNumberAndSeriesId(seasonNumber, seriesId);
    }

    public List<Episode> getSeasonEpisodes(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new EntityNotFoundException("Series not found");
        }
        return episodeRepository.findAllEpisodesBySeriesIdAndSeasonNumber(seriesId, seasonNumber);
    }

    public Episode getEpisodeByNumberAndSeasonNumberAndSeriesId(int seriesId, int seasonNumber, int episodeNumber) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new EntityNotFoundException("Series not found");
        }
        return episodeRepository.findEpisodeByNumberAndSeasonNumberAndSeriesId(seriesId, seasonNumber, episodeNumber);
    }

    public int getEpisodeCountBySeasonId(int seasonId) {
        return episodeRepository.countBySeasonId(seasonId);
    }

    private ContentType getContentTypeOrThrow(int contentId) {
        return contentTypeRepository.findContentTypeById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
    }

}
