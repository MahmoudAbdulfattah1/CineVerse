package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.exception.crew.CrewMemberNotFoundException;
import com.cineverse.cineverse.exception.crew.SocialLinksNotFoundException;
import com.cineverse.cineverse.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CrewMemberService {
    private CrewMemberSocialRepository crewMemberSocialRepository;
    private CrewMemberRepository crewMemberRepository;
    private ContentCastRepository contentCastRepository;
    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;

    public CrewMember getCrewMemberDetails(int crewMemberId) {
        return crewMemberRepository.findByIdWithAliases(crewMemberId)
                .orElseThrow(() -> new CrewMemberNotFoundException("Crew member not found with id: " + crewMemberId));
    }

    public CrewMemberSocial getCrewMemberSocial(int crewMemberId) {
        return crewMemberSocialRepository.findById(crewMemberId)
                .orElseThrow(() -> new SocialLinksNotFoundException("Crew member social not found with id: " + crewMemberId));
    }

    public Page<Content> getCrewMemberContents(int crewMemberId, ContentType contentType, Pageable pageable) {
        CrewMember crewMember = crewMemberRepository.findById(crewMemberId)
                .orElseThrow(() -> new CrewMemberNotFoundException("Crew member not found with id: " + crewMemberId));

        List<Content> contents = new ArrayList<>();

        boolean includeMovies = contentType == null || contentType.equals(ContentType.MOVIE);
        boolean includeSeries = contentType == null || contentType.equals(ContentType.SERIES);

        if (includeMovies) {
            List<Movie> movies = switch (crewMember.getKnownForDepartment()) {
                case "Directing" -> movieRepository.findByDirectorId(crewMemberId);
                case "Acting" -> contentCastRepository.findMoviesByCastId(crewMemberId);
                default -> Collections.emptyList();
            };
            contents.addAll(movies);
        }

        if (includeSeries) {
            List<Series> series = switch (crewMember.getKnownForDepartment()) {
                case "Directing" -> seriesRepository.findByDirectorId(crewMemberId);
                case "Acting" -> contentCastRepository.findSeriesByCastId(crewMemberId);
                default -> Collections.emptyList();
            };
            contents.addAll(series);
        }

        contents.sort(Comparator.comparing(Content::getReleaseDate).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), contents.size());
        List<Content> pageContent = start < end ? contents.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(pageContent, pageable, contents.size());
    }


}
