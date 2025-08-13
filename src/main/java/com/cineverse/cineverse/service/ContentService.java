package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.document.ContentDocument;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentStatus;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.exception.content.UnsupportedContentTypeException;
import com.cineverse.cineverse.infrastructure.youtube.YoutubeClient;
import com.cineverse.cineverse.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ContentService {
    private ContentRepository contentRepository;
    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;
    private SeasonRepository seasonRepository;
    private EpisodeRepository episodeRepository;
    private ContentTypeRepository contentTypeRepository;
    private ContentTrailerRepository contentTrailerRepository;
    private final ContentDocumentRepository documentRepository;
    private YoutubeClient youtubeClient;

    /**
     * Filters content based on given criteria.
     *
     * @param genres      list of genre names to filter by (nullable)
     * @param year        release year filter (nullable)
     * @param rate        minimum rating filter (nullable)
     * @param contentType type of content (MOVIE, SERIES, etc.) (nullable)
     * @param language    ISO language code filter (nullable)
     * @param sortBy      field to sort results by (nullable)
     * @param status      content status (e.g., RELEASED, UPCOMING) (nullable)
     * @param order       sorting order ("asc" or "desc") (nullable)
     * @param page        page number for pagination
     * @param size        page size for pagination
     * @return a paginated list of {@link Content} matching the filters
     */
    public Page<Content> filterContent(List<String> genres, Integer year, Integer rate,
                                       ContentType contentType, String language, String sortBy, ContentStatus status,
                                       String order, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contentRepository.filterContent(genres, year, rate, contentType, language, sortBy, status, order,
                pageable);
    }

    /**
     * Searches for content documents by keyword in Elasticsearch.
     *
     * @param keyword the search term; if null or blank, returns an empty page
     * @param page    page number for pagination
     * @param size    page size for pagination
     * @return a paginated list of {@link ContentDocument} matching the keyword
     */
    public Page<ContentDocument> searchContent(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isBlank()) {
            return Page.empty(pageable);
        }
        return documentRepository.searchByTitle(keyword, pageable);
    }

    /**
     * Retrieves movie details including genres by its slug.
     *
     * @param movieSlug unique movie slug
     * @return the {@link Movie} details
     */
    public Movie getMovieDetails(String movieSlug) {
        return movieRepository.findMovieWithGenres(movieSlug);
    }

    /**
     * Retrieves series details including genres by its slug.
     *
     * @param slug unique series slug
     * @return the {@link Series} details
     */
    public Series getSeriesDetails(String slug) {
        return seriesRepository.findSeriesWithGenres(slug);
    }

    /**
     * Retrieves either movie or series details by slug.
     *
     * @param slug unique slug of the content
     * @return the {@link Content} details
     * @throws ContentNotFoundException if neither a movie nor a series is found
     */
    public Content getContentDetails(String slug) {
        if (movieRepository.existsBySlug(slug)) {
            return getMovieDetails(slug);
        } else if (seriesRepository.existsBySlug(slug)) {
            return getSeriesDetails(slug);
        }
        throw new ContentNotFoundException("Content is not found");
    }

    /**
     * Retrieves the YouTube trailer ID for a given content.
     * If not found in the database, fetches from YouTube API using content metadata.
     *
     * @param contentId ID of the content
     * @return the YouTube trailer ID
     * @throws ContentNotFoundException        if the content is not found
     * @throws UnsupportedContentTypeException if the content type is not supported
     */
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

    /**
     * Retrieves the platform rating for a given content, rounded to one decimal place.
     *
     * @param contentId ID of the content
     * @return the rounded rating
     */
    public float getPlatformRate(int contentId) {
        float rate = contentRepository.getPlatformRate(contentId);
        return Math.round(rate * 10) / 10.0f;
    }

    /**
     * Gets the total number of reviews for a content.
     *
     * @param contentId ID of the content
     * @return total reviews count
     */
    public int getContentTotalReviewsCount(int contentId) {
        return contentRepository.totalReviewsCount(contentId);
    }

    /**
     * Gets the total number of watchlist entries for a content.
     *
     * @param contentId ID of the content
     * @return total watchlist count
     */
    public int getContentTotalWatchlistCount(int contentId) {
        return contentRepository.totalWatchlistCount(contentId);
    }

    /**
     * Retrieves the director of a given content.
     *
     * @param contentId ID of the content
     * @return the {@link CrewMember} representing the director
     * @throws UnsupportedContentTypeException if the content type is not MOVIE or SERIES
     */
    public CrewMember getContentDirector(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieDirector(contentId);
            case SERIES -> seriesRepository.findSeriesDirector(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    /**
     * Retrieves the cast list for a given content.
     *
     * @param contentId ID of the content
     * @return list of {@link ContentCast} members
     * @throws UnsupportedContentTypeException if the content type is not MOVIE or SERIES
     */
    public List<ContentCast> getContentCast(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieCast(contentId);
            case SERIES -> seriesRepository.findSeriesCast(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    /**
     * Retrieves the providers for a given content.
     *
     * @param contentId ID of the content
     * @return list of {@link Provider}
     * @throws UnsupportedContentTypeException if the content type is not MOVIE or SERIES
     */
    public List<Provider> getContentProviders(int contentId) {
        return switch (getContentTypeOrThrow(contentId)) {
            case MOVIE -> movieRepository.findMovieProviders(contentId);
            case SERIES -> seriesRepository.findSeriesProviders(contentId);
            default -> throw new UnsupportedContentTypeException("Unsupported content type.");
        };
    }

    /**
     * Retrieves all seasons for a given series.
     *
     * @param seriesId ID of the series
     * @return list of {@link Season}
     * @throws ContentNotFoundException if the series does not exist
     */
    public List<Season> getSeasonsBySeriesId(int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return seasonRepository.findAllSeasonsBySeriesId(seriesId);
    }

    /**
     * Retrieves a season by its number and series ID.
     *
     * @param seasonNumber season number
     * @param seriesId     ID of the series
     * @return the {@link Season}
     * @throws ContentNotFoundException if the series does not exist
     */
    public Season getSeasonByNumberAndSeriesId(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return seasonRepository.findSeasonBySeasonNumberAndSeriesId(seasonNumber, seriesId);
    }

    /**
     * Retrieves all episodes for a given season number in a series.
     *
     * @param seasonNumber season number
     * @param seriesId     ID of the series
     * @return list of {@link Episode}
     * @throws ContentNotFoundException if the series does not exist
     */
    public List<Episode> getSeasonEpisodes(int seasonNumber, int seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return episodeRepository.findAllEpisodesBySeriesIdAndSeasonNumber(seriesId, seasonNumber);
    }

    /**
     * Retrieves an episode by episode number, season number, and series ID.
     *
     * @param seriesId      ID of the series
     * @param seasonNumber  season number
     * @param episodeNumber episode number
     * @return the {@link Episode}
     * @throws ContentNotFoundException if the series does not exist
     */
    public Episode getEpisodeByNumberAndSeasonNumberAndSeriesId(int seriesId, int seasonNumber, int episodeNumber) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ContentNotFoundException("Series not found");
        }
        return episodeRepository.findEpisodeByNumberAndSeasonNumberAndSeriesId(seriesId, seasonNumber, episodeNumber);
    }

    /**
     * Counts the number of episodes in a given season.
     *
     * @param seasonId ID of the season
     * @return episode count
     */
    public int getEpisodeCountBySeasonId(int seasonId) {
        return episodeRepository.countBySeasonId(seasonId);
    }

    /**
     * Retrieves content type by ID.
     *
     * @param contentId ID of the content
     * @return the {@link ContentType}
     * @throws ContentNotFoundException if no content is found for the given type and ID
     */
    public ContentType getContentTypeOrThrow(int contentId) {
        return contentTypeRepository.findContentTypeById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("Content not found"));
    }

    /**
     * Retrieves typed content by ID and content type.
     *
     * @param contentId   ID of the content
     * @param contentType the type of content
     * @return the {@link Content}
     * @throws ContentNotFoundException if no content is found for the given type and ID
     */
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
