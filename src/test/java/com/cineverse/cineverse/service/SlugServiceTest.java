package com.cineverse.cineverse.service;

import com.cineverse.cineverse.repository.MovieRepository;
import com.cineverse.cineverse.repository.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class SlugServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private SlugService slugService;

    @Test
    void generateUniqueSlug_shouldReturnBaseSlug_whenNotExists() {
        when(movieRepository.existsBySlug("the-matrix")).thenReturn(false);
        when(seriesRepository.existsBySlug("the-matrix")).thenReturn(false);

        String result = slugService.generateUniqueSlug("The Matrix");

        assertEquals("the-matrix", result);
    }

    @Test
    void generateUniqueSlug_shouldReturnIncrementedSlug_whenExists() {
        when(movieRepository.existsBySlug("the-matrix")).thenReturn(true);
        when(movieRepository.existsBySlug("the-matrix-1")).thenReturn(false);
        when(seriesRepository.existsBySlug("the-matrix-1")).thenReturn(false);

        String result = slugService.generateUniqueSlug("The Matrix");

        assertEquals("the-matrix-1", result);
        verify(movieRepository).existsBySlug("the-matrix");
        verify(movieRepository).existsBySlug("the-matrix-1");
        verify(seriesRepository).existsBySlug("the-matrix-1");
    }
}
