package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CrewMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tmdb_id", unique = true)
    private int tmdbId;
    private String name;
    @Column(name = "profile_path")
    private String profilePath;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(length = 3000)
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;
    private String placeOfBirth;
    private String knownForDepartment;
    @OneToMany(mappedBy = "cast", fetch = FetchType.LAZY)
    private Set<ContentCast> contentCasts;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private Set<Series> series;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private Set<Movie> movies;

    public CrewMember(int tmdbId, String name, String profilePath, Gender gender, String biography,
                      LocalDate birthday, LocalDate deathday, String placeOfBirth, String knownForDepartment) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.profilePath = profilePath;
        this.gender = gender;
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.placeOfBirth = placeOfBirth;
        this.knownForDepartment = knownForDepartment;
    }
}
