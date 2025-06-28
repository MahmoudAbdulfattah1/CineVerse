package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    @Column(length = 5000)
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;
    private String placeOfBirth;
    private String knownForDepartment;
    @ElementCollection
    @CollectionTable(
            name = "crew_member_aliases",
            joinColumns = @JoinColumn(name = "crew_member_id")
    )
    @Column(name = "alias")
    private List<String> alsoKnownAs;
    @OneToMany(mappedBy = "cast", fetch = FetchType.LAZY)
    private List<ContentCast> contentCasts;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<Series> series;
    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<Movie> movies;

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
