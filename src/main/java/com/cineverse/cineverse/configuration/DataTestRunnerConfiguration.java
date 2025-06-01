package com.cineverse.cineverse.configuration;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.repository.*;
import com.cineverse.cineverse.service.SlugService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataTestRunnerConfiguration {
    @Bean
    public CommandLineRunner testEntityStructure(
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            CrewMemberRepository crewMemberRepository,
            ProviderRepository providerRepository,
            ContentCastRepository contentCastRepository,
            ContentProviderRepository contentProviderRepository,
            ContentGenreRepository contentGenreRepository,
            SlugService slugService
    ) {
        return args -> {
            System.out.println("=== Testing Entity Relationships ===");
            CrewMember director = new CrewMember(223940, "Ali Ragab", "/vMOHnKRk09IAcb314OGx5T2gnqT.jpg");
            crewMemberRepository.save(director);
            Movie movie = new Movie(514025,
                    "كركر",
                    "Karkar young rich who caused a shock Electric insanity, and after the death of his father, uncle trying Asim exploit his illness and fraud on him to get his money",
                    LocalDate.now(),
                    "/rRdZi3MOf54SZtbTjGHtOSFZkWl.jpg",
                    "ar",
                    6,
                    120,
                    "Egypt",
                    director);
            movie.setSlug(slugService.generateUniqueSlugForMovie(movie.getTitle()));
            CrewMember yasminAbdulaziz = new CrewMember(140879, "Yasmin Abdulaziz", "/uTErKrdz1wG5a2lwrn2Mm4Og8vU.jpg");
            crewMemberRepository.save(yasminAbdulaziz);
            ContentCast cast = new ContentCast("اوشين", yasminAbdulaziz, movie);
            CrewMember mohamedSaad = crewMemberRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Cast not found"));
            Set<ContentCast> casts = new HashSet<>();
            casts.add(cast);
            casts.add(new ContentCast("كركر و الحناوي", mohamedSaad, movie));
            movie.setContentCasts(casts);
            Provider netflix = new Provider(151525, "Netflix", "/uTErKrdz1wG5a2lwrn2Mm4Og8vU.jpg");
            Provider shahid = new Provider(153525, "Shahid", "/uTErKrdz1wG5a2lwrn2Mm4Og8vU.jpg");
            Genre drama = genreRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
            Genre comedy = genreRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
            List<ContentGenre> genres = List.of(new ContentGenre(drama, movie), new ContentGenre(comedy, movie));
            providerRepository.saveAll(List.of(netflix, shahid));
            List<ContentProvider> providers = List.of(new ContentProvider(netflix, movie), new ContentProvider(shahid, movie));
            movieRepository.save(movie);
            contentCastRepository.saveAll(casts);
            contentProviderRepository.saveAll(providers);
            contentGenreRepository.saveAll(genres);
        };
    }
}
