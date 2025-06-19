package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CrewMemberDto {
    private int id;
    private String name;
    private String path;
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;
    private String placeOfBirth;
    private String knownForDepartment;
    private List<String> alsoKnownAs;

    public CrewMemberDto(int id, String name, String path, String biography, LocalDate birthday, LocalDate deathday,
                         String placeOfBirth, String knownForDepartment, List<String> alsoKnownAs) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.placeOfBirth = placeOfBirth;
        this.knownForDepartment = knownForDepartment;
        this.alsoKnownAs = alsoKnownAs;
    }
}
