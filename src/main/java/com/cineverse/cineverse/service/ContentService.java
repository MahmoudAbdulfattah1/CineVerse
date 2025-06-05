package com.cineverse.cineverse.service;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.*;
import com.cineverse.cineverse.repository.*;
import com.cineverse.cineverse.service.mapper.CreditMapper;
import com.cineverse.cineverse.service.mapper.ProviderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ContentService {
    private ContentRepository contentRepository;
    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;
    private ReviewRepository reviewRepository;
    private ContentTypeRepository contentTypeRepository;
    private YoutubeService youtubeService;
    private final TMDBApiConfiguration tmdbApiConfiguration;
    private final CreditMapper creditMapper;
    private final ProviderMapper providerMapper;

    public List<ContentMetaDataDto> filterContent(List<String> genres, Integer year, Integer rate, ContentType contentType, String language, String sortBy) {
        return contentRepository.filterContent(genres, year, rate, contentType, language, sortBy);
    }

    public List<ContentMetaDataDto> searchContent(String keyword) {
        return contentRepository.searchContent(keyword);
    }

    public MovieDto getMovieDetails(String movieSlug) {
        Movie movie = movieRepository.findMovieWithGenres(movieSlug);
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                tmdbApiConfiguration.getBaseImageUrl() + movie.getPosterPath(),
                tmdbApiConfiguration.getBaseImageUrl() + movie.getBackdropPath(),
                movie.getReleaseDate(),
                movie.getRuntime(),
                movie.getLanguage(),
                movie.getProductionCountry(),
                movie.getImdbRate(),
                getPlatformRate(movie.getId()),
                movie.getGenres().stream().map(genre -> genre.getGenre().getName())
                        .toList()
        );
    }

    public SeriesDto getSeriesDetails(String slug) {
        Series series = seriesRepository.findSeriesWithGenres(slug);
        return new SeriesDto(
                series.getId(),
                series.getTitle(),
                series.getOverview(),
                tmdbApiConfiguration.getBaseImageUrl() + series.getPosterPath(),
                tmdbApiConfiguration.getBaseImageUrl() + series.getBackdropPath(),
                series.getReleaseDate(),
                series.getLanguage(),
                series.getProductionCountry(),
                series.getImdbRate(),
                getPlatformRate(series.getId()),
                series.getNumberOfSeasons(),
                series.getNumberOfEpisodes(),
                series.getStatus(),
                series.getGenres().stream().map(genre -> genre.getGenre().getName())
                        .toList()
        );
    }

    public ContentDetailsDto getContentDetails(String slug) {
        boolean movie = movieRepository.existsBySlug(slug);
        boolean series = seriesRepository.existsBySlug(slug);
        return movie ?
                getMovieDetails(slug) :
                series ? getSeriesDetails(slug) :
                        null;
    }

    public float getPlatformRate(int contentId) {
        float rate = contentRepository.getPlatformRate(contentId);
        return Math.round(rate * 10) / 10.0f;
    }

    public List<ProviderDto> getContentProviders(int contentId) {
        return providerMapper.map(contentRepository.findContentProviders(contentId));
    }

    public TrailerDto getContentTrailer(int contentId) {
        return new TrailerDto(youtubeService.getTrailerUrl(contentRepository.findContentTitleWithId(contentId)));
    }

    public ContentStatsDto getContentStats(int contentId) {
        return new ContentStatsDto(
                contentRepository.totalReviews(contentId),
                contentRepository.totalWatchlists(contentId)
        );
    }

    public List<ReviewDto> getContentReviews(int contentId) {
        return reviewRepository.findContentReviews(contentId);
    }

    public CastAndCrewDto getContentCredits(int contentId) {
        ContentType type = contentTypeRepository.findContentTypeById(contentId).orElseThrow(
                () -> new EntityNotFoundException("Content not found"));
        return type.equals(ContentType.MOVIE) ?
                new CastAndCrewDto(creditMapper.map(movieRepository.findMovieDirector(contentId)), creditMapper.map(movieRepository.findMovieCasts(contentId))) :
                type.equals(ContentType.SERIES) ?
                        new CastAndCrewDto(creditMapper.map(seriesRepository.findSeriesDirector(contentId)), creditMapper.map(seriesRepository.findSeriesCasts(contentId))) :
                        null;
    }


}
