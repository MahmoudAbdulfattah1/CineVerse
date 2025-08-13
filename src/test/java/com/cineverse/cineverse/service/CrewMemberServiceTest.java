package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.exception.crew.CrewMemberNotFoundException;
import com.cineverse.cineverse.exception.crew.SocialLinksNotFoundException;
import com.cineverse.cineverse.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewMemberServiceTest {
    @Mock
    private CrewMemberRepository crewMemberRepository;
    @Mock
    private CrewMemberSocialRepository crewMemberSocialRepository;
    @Mock
    private ContentCastRepository contentCastRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private CrewMemberService crewMemberService;

    private CrewMember mockCrewMember;
    private CrewMember mockDirector;
    private CrewMember mockProducer;
    private CrewMemberSocial mockCrewMemberSocial;
    private Movie movie1;
    private Movie movie2;
    private Series series1;
    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 10);


    @BeforeEach
    void setUpCrewMembers() {
        mockCrewMember = new CrewMember();
        mockCrewMember.setId(1);
        mockCrewMember.setName("Mohammed Saad");
        mockCrewMember.setAlsoKnownAs(List.of("Mohamed Saad", "El-Limby"));
        mockCrewMember.setKnownForDepartment("Acting");
        mockCrewMemberSocial = new CrewMemberSocial();
        mockCrewMemberSocial.setCrewMember(mockCrewMember);
        mockCrewMemberSocial.setFacebookId("mohammed.saad");

        mockDirector = new CrewMember();
        mockDirector.setId(2);
        mockDirector.setKnownForDepartment("Directing");

        mockProducer = new CrewMember();
        mockProducer.setId(3);
        mockProducer.setKnownForDepartment("Producing");

    }

    @BeforeEach
    void setUpContentData() {
        movie1 = new Movie();
        movie1.setReleaseDate(LocalDate.of(2023, 1, 1));
        movie2 = new Movie();
        movie2.setReleaseDate(LocalDate.of(2024, 1, 1));
        series1 = new Series();
        series1.setReleaseDate(LocalDate.of(2022, 1, 1));
    }

    @Test
    void getCrewMemberDetails_shouldReturnCrewMember_whenExists() {
        when(crewMemberRepository.findByIdWithAliases(1)).thenReturn(Optional.of(mockCrewMember));

        CrewMember result = crewMemberService.getCrewMemberDetails(1);

        assertNotNull(result);
        assertEquals("Mohammed Saad", result.getName());
        assertEquals(1, result.getId());
        assertEquals(List.of("Mohamed Saad", "El-Limby"), result.getAlsoKnownAs());
        verify(crewMemberRepository, times(1)).findByIdWithAliases(1);
    }

    @Test
    void getCrewMemberDetails_shouldThrowException_whenNotFound() {
        when(crewMemberRepository.findByIdWithAliases(2)).thenReturn(Optional.empty());

        assertThrows(CrewMemberNotFoundException.class, () -> crewMemberService.getCrewMemberDetails(2));
        verify(crewMemberRepository, times(1)).findByIdWithAliases(2);
    }

    @Test
    void getCrewMemberSocial_shouldReturnSocialLinks_whenExists() {
        when(crewMemberSocialRepository.findById(1)).thenReturn(Optional.of(mockCrewMemberSocial));

        CrewMemberSocial result = crewMemberService.getCrewMemberSocial(1);

        assertEquals(mockCrewMemberSocial, result);
        assertEquals("mohammed.saad", mockCrewMemberSocial.getFacebookId());
        verify(crewMemberSocialRepository, times(1)).findById(1);
    }

    @Test
    void getCrewMemberSocial_shouldThrowException_whenNotFound() {
        when(crewMemberSocialRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(SocialLinksNotFoundException.class, () -> crewMemberService.getCrewMemberSocial(2));
        verify(crewMemberSocialRepository, times(1)).findById(2);
    }

    @Test
    void getCrewMemberContents_shouldReturnMoviesAndSeries_whenDirectingAndContentTypeIsNull() {
        when(crewMemberRepository.findById(2)).thenReturn(Optional.of(mockDirector));
        when(movieRepository.findByDirectorId(2)).thenReturn(List.of(movie1, movie2));
        when(seriesRepository.findByDirectorId(2)).thenReturn(List.of(series1));

        Page<Content> result = crewMemberService.getCrewMemberContents(2, null, PAGE_REQUEST);

        assertEquals(3, result.getTotalElements());
        assertEquals(movie2, result.getContent().get(0));
    }

    @Test
    void getCrewMemberContents_shouldReturnMoviesOnly_whenActingAndContentTypeIsMovie() {
        when(crewMemberRepository.findById(1)).thenReturn(Optional.of(mockCrewMember));
        when(contentCastRepository.findMoviesByCastId(1)).thenReturn(List.of(movie1, movie2));

        Page<Content> result = crewMemberService.getCrewMemberContents(1, ContentType.MOVIE, PAGE_REQUEST);

        assertEquals(2, result.getTotalElements());
        assertEquals(movie2, result.getContent().get(0));
        verify(contentCastRepository, times(1)).findMoviesByCastId(1);
    }

    @Test
    void getCrewMemberContents_shouldReturnEmptySeriesOnly_whenProducingAndContentTypeIsSeries() {
        when(crewMemberRepository.findById(3)).thenReturn(Optional.of(mockProducer));

        Page<Content> result = crewMemberService.getCrewMemberContents(3, ContentType.SERIES, PAGE_REQUEST);

        assertEquals(0, result.getTotalElements());
        verify(contentCastRepository, never()).findSeriesByCastId(3);
    }

    @Test
    void getCrewMemberContents_shouldThrowException_whenCrewMemberNotFound() {
        when(crewMemberRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(CrewMemberNotFoundException.class, () -> crewMemberService.getCrewMemberContents(4,
                ContentType.MOVIE, PAGE_REQUEST));
        verify(crewMemberRepository, times(1)).findById(4);
    }
}
