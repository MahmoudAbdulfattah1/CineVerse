package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.document.ContentDocument;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.exception.content.ContentNotFoundException;
import com.cineverse.cineverse.infrastructure.youtube.YoutubeClient;
import com.cineverse.cineverse.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {
    @Mock
    ContentRepository contentRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    SeriesRepository seriesRepository;
    @Mock
    SeasonRepository seasonRepository;
    @Mock
    EpisodeRepository episodeRepository;
    @Mock
    ContentTypeRepository contentTypeRepository;
    @Mock
    ContentTrailerRepository contentTrailerRepository;
    @Mock
    ContentDocumentRepository documentRepository;
    @Mock
    YoutubeClient youtubeClient;

    @InjectMocks
    private ContentService contentService;

    private Content mockMovieContent;
    private Movie mockMovie;
    private Series mockSeries;
    private Season mockSeason;
    private Episode mockEpisode;

    private CrewMember mockDirector;
    private CrewMember mockCast;
    private ContentCast mockContentCast;
    private Provider mockProvider;

    private ContentTrailer mockContentTrailer;

    @BeforeEach
    void setUp() {
        mockDirector = new CrewMember();
        mockDirector.setId(1);
        mockDirector.setName("director");

        mockCast = new CrewMember();
        mockCast.setId(2);
        mockCast.setName("cast");

        mockMovieContent = new Content();
        mockMovieContent.setId(1);
        mockMovieContent.setTitle("Inception");
        mockMovieContent.setContentType(ContentType.MOVIE);
        mockMovie = new Movie();
        mockMovie.setId(1);
        mockMovie.setSlug("inception");
        mockMovie.setLanguage("en");
        mockMovie.setReleaseDate(LocalDate.of(2010, 7, 16));
        mockMovie.setGenres(Set.of());
        mockMovie.setDirector(mockDirector);

        mockSeries = new Series();
        mockSeries.setId(2);
        mockSeries.setSlug("breaking-bad");

        mockSeason = new Season();
        mockSeason.setId(3);
        mockSeason.setSeasonNumber(1);

        mockEpisode = new Episode();
        mockEpisode.setId(4);
        mockEpisode.setEpisodeNumber(1);

        mockContentTrailer = new ContentTrailer();
        mockContentTrailer.setId(1L);
        mockContentTrailer.setContent(mockMovie);
        mockContentTrailer.setYoutubeId("trailer123");

        mockContentCast = new ContentCast();
        mockContentCast.setId(1);
        mockContentCast.setContent(mockSeries);
        mockContentCast.setCast(mockCast);

        mockProvider = new Provider();
        mockProvider.setId(1);
        mockProvider.setName("Netflix");
    }

    @Test
    void searchContent_shouldReturnEmptyList_whenNoContentFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContentDocument> documentPage = new PageImpl<>(List.of());

        when(documentRepository.searchByTitle("superman", pageable)).thenReturn(documentPage);

        Page<ContentDocument> result = contentService.searchContent("superman", 0, 10);

        assertEquals(0, result.getContent().size());
        verify(documentRepository, times(1)).searchByTitle(eq("superman"), any(Pageable.class));
    }

    @Test
    void searchContent_shouldReturnContent_whenContentFound() {
        Pageable pageable = PageRequest.of(0, 10);
        ContentDocument document = ContentDocument.builder().
                id(String.valueOf(mockMovieContent.getId()))
                .title(mockMovieContent.getTitle())
                .contentType(mockMovieContent.getContentType().toString())
                .build();
        Page<ContentDocument> documentPage = new PageImpl<>(List.of(document));

        when(documentRepository.searchByTitle("Inception", pageable)).thenReturn(documentPage);

        Page<ContentDocument> result = contentService.searchContent("Inception", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Inception", result.getContent().get(0).getTitle());
        assertEquals(mockMovieContent.getContentType().toString(), result.getContent().get(0).getContentType());
        verify(documentRepository, times(1)).searchByTitle(eq("Inception"), any(Pageable.class));
    }

    @Test
    void getContentDetails_shouldThrowException_whenContentNotFound() {
        when(movieRepository.existsBySlug("anySlug")).thenReturn(false);
        when(seriesRepository.existsBySlug("anySlug")).thenReturn(false);

        assertThrows(ContentNotFoundException.class, () -> contentService.getContentDetails("anySlug"));
        verify(movieRepository, times(1)).existsBySlug("anySlug");
        verify(seriesRepository, times(1)).existsBySlug("anySlug");
    }

    @Test
    void getContentDetails_shouldReturnSeriesDetails_whenSeriesExists() {
        when(movieRepository.existsBySlug("breaking-bad")).thenReturn(false);
        when(seriesRepository.existsBySlug("breaking-bad")).thenReturn(true);
        when(seriesRepository.findSeriesWithGenres("breaking-bad")).thenReturn(mockSeries);

        Content result = contentService.getContentDetails("breaking-bad");

        assertEquals(mockSeries, result);
        assertEquals(2, result.getId());
        verify(movieRepository, times(1)).existsBySlug("breaking-bad");
        verify(seriesRepository, times(1)).existsBySlug("breaking-bad");
    }

    @Test
    void getContentTrailer_shouldReturnTrailer_whenExistsInDatabase() {
        when(contentTrailerRepository.findYoutubeIdByContentId(1)).thenReturn(mockContentTrailer.getYoutubeId());
        String result = contentService.getContentTrailer(1);

        assertEquals("trailer123", result);
        verify(contentTrailerRepository, times(1)).findYoutubeIdByContentId(1);
        verify(youtubeClient, never()).getTrailerUrl(any(String.class), any(Integer.class),
                any(ContentType.class), any(String.class), any());
    }

    @Test
    void getContentTrailer_shouldReturnNull_whenTrailerNotExists() {
        when(contentTrailerRepository.findYoutubeIdByContentId(1)).thenReturn(null);
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.of(ContentType.MOVIE));
        when(movieRepository.findMovieById(1)).thenReturn(mockMovie);

        String result = contentService.getContentTrailer(1);

        assertNull(result);
        verify(contentTrailerRepository, times(1)).findYoutubeIdByContentId(1);
    }


    @Test
    void getContentTrailer_shouldReturnTrailer_whenExistsInYoutubeClient() {
        when(contentTrailerRepository.findYoutubeIdByContentId(1)).thenReturn(null);
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.of(ContentType.MOVIE));
        when(movieRepository.findMovieById(1)).thenReturn(mockMovie);
        when(youtubeClient.getTrailerUrl(any(), anyInt(), any(), any(), anyList()))
                .thenReturn("https://youtube.com/watch?v=trailer123");

        String result = contentService.getContentTrailer(1);

        assertEquals("https://youtube.com/watch?v=trailer123", result);
        verify(contentTrailerRepository, times(1)).findYoutubeIdByContentId(1);
        verify(contentTypeRepository, times(1)).findContentTypeById(1);
        verify(movieRepository, times(1)).findMovieById(1);
    }

    @Test
    void getPlatformRate_shouldReturnZero_whenNoReviewsFound() {
        when(contentRepository.getPlatformRate(1)).thenReturn(0f);

        double result = contentService.getPlatformRate(1);

        assertEquals(0f, result);
        verify(contentRepository, times(1)).getPlatformRate(1);
    }

    @Test
    void getPlatformRate_shouldReturnCorrectRate_whenReviewsExist() {
        when(contentRepository.getPlatformRate(1)).thenReturn(4.5f);

        double result = contentService.getPlatformRate(1);

        assertEquals(4.5f, result);
        verify(contentRepository, times(1)).getPlatformRate(1);
    }

    @Test
    void getContentDirector_shouldThrowException_whenContentTypeNotFound() {
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.empty());

        assertThrows(ContentNotFoundException.class, () -> contentService.getContentDirector(1));
        verify(contentTypeRepository, times(1)).findContentTypeById(1);
    }

    @Test
    void getContentDirector_shouldReturnDirector_whenExists() {
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.of(ContentType.MOVIE));
        when(movieRepository.findMovieDirector(1)).thenReturn(mockDirector);

        CrewMember result = contentService.getContentDirector(1);

        assertEquals(mockDirector, result);
        assertEquals(1, result.getId());
        assertEquals("director", result.getName());
        verify(movieRepository, times(1)).findMovieDirector(1);
    }

    @Test
    void getContentCast_shouldThrowException_whenContentNotFound() {
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.empty());

        assertThrows(ContentNotFoundException.class, () -> contentService.getContentCast(1));
        verify(contentTypeRepository, times(1)).findContentTypeById(1);
    }

    @Test
    void getContentCast_shouldReturnCast_whenExists() {
        when(contentTypeRepository.findContentTypeById(2)).thenReturn(Optional.of(ContentType.SERIES));
        when(seriesRepository.findSeriesCast(2)).thenReturn(List.of(mockContentCast));

        List<ContentCast> result = contentService.getContentCast(2);

        assertEquals(1, result.size());
        assertEquals(mockCast, result.get(0).getCast());
        assertEquals(mockSeries, result.get(0).getContent());
        verify(seriesRepository, times(1)).findSeriesCast(2);
    }

    @Test
    void getContentProvider_shouldThrowException_whenContentNotFound() {
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.empty());

        assertThrows(ContentNotFoundException.class, () -> contentService.getContentCast(1));
        verify(contentTypeRepository, times(1)).findContentTypeById(1);
        verify(movieRepository, never()).findMovieProviders(anyInt());
        verify(seriesRepository, never()).findSeriesProviders(anyInt());
    }

    @Test
    void getContentProvider_shouldReturnProvider_whenExists() {
        when(contentTypeRepository.findContentTypeById(1)).thenReturn(Optional.of(ContentType.MOVIE));
        when(movieRepository.findMovieProviders(1)).thenReturn(List.of(mockProvider));

        List<Provider> result = contentService.getContentProviders(1);

        assertEquals(1, result.size());
        assertEquals(mockProvider, result.get(0));
        verify(movieRepository, times(1)).findMovieProviders(1);
    }

    @Test
    void getSeasonsBySeriesId_shouldThrowException_whenSeriesNotFound() {
        when(seriesRepository.existsById(6)).thenReturn(false);

        assertThrows(ContentNotFoundException.class, () -> contentService.getSeasonsBySeriesId(6));
        verify(seriesRepository, times(1)).existsById(6);
    }

    @Test
    void getSeasonsBySeriesId_shouldReturnEmptyList_whenNoSeasonsFound() {
        when(seriesRepository.existsById(1)).thenReturn(true);
        when(seasonRepository.findAllSeasonsBySeriesId(1)).thenReturn(List.of());

        List<Season> result = contentService.getSeasonsBySeriesId(1);

        assertTrue(result.isEmpty());
        verify(seasonRepository, times(1)).findAllSeasonsBySeriesId(1);
    }

    @Test
    void getSeasonsBySeriesId_shouldReturnSeasons_whenSeasonsExist() {
        when(seriesRepository.existsById(1)).thenReturn(true);
        when(seasonRepository.findAllSeasonsBySeriesId(1)).thenReturn(List.of(mockSeason));

        List<Season> result = contentService.getSeasonsBySeriesId(1);

        assertEquals(1, result.size());
        assertEquals(mockSeason, result.get(0));
        verify(seasonRepository, times(1)).findAllSeasonsBySeriesId(1);
    }

    @Test
    void getSeasonByNumberAndSeriesId_shouldThrowException_whenSeriesNotFound() {
        when(seriesRepository.existsById(2)).thenReturn(false);

        assertThrows(ContentNotFoundException.class, () -> contentService.getSeasonByNumberAndSeriesId(1, 2));
        verify(seriesRepository, times(1)).existsById(2);
        verify(seasonRepository, never()).findSeasonBySeasonNumberAndSeriesId(anyInt(), anyInt());
    }
    @Test
    void getSeasonByNumberAndSeriesId_shouldReturnSeason_whenExists() {
        when(seriesRepository.existsById(1)).thenReturn(true);
        when(seasonRepository.findSeasonBySeasonNumberAndSeriesId(1, 1)).thenReturn(mockSeason);

        Season result = contentService.getSeasonByNumberAndSeriesId(1, 1);

        assertEquals(mockSeason, result);
        assertEquals(3, result.getId());
        assertEquals(1, result.getSeasonNumber());
        verify(seasonRepository, times(1)).findSeasonBySeasonNumberAndSeriesId(1, 1);
    }

    @Test
    void getEpisodeByNumberAndSeasonNumberAndSeriesId_shouldThrowException_whenSeriesNotFound() {
        when(seriesRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ContentNotFoundException.class, () -> contentService.getEpisodeByNumberAndSeasonNumberAndSeriesId(1, 1, 2));
        verify(seriesRepository, times(1)).existsById(anyInt());
        verify(episodeRepository, never()).findEpisodeByNumberAndSeasonNumberAndSeriesId(anyInt(), anyInt(), anyInt());
    }
    @Test
    void getEpisodeByNumberAndSeasonNumberAndSeriesId_shouldReturnEpisode_whenExists() {
        when(seriesRepository.existsById(1)).thenReturn(true);
        when(episodeRepository.findEpisodeByNumberAndSeasonNumberAndSeriesId(1, 1, 1)).thenReturn(mockEpisode);

        Episode result = contentService.getEpisodeByNumberAndSeasonNumberAndSeriesId(1, 1, 1);

        assertEquals(mockEpisode, result);
        assertEquals(4, result.getId());
        assertEquals(1, result.getEpisodeNumber());
        verify(episodeRepository, times(1)).findEpisodeByNumberAndSeasonNumberAndSeriesId(1, 1, 1);
    }

}
