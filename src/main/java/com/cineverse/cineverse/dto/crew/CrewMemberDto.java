package com.cineverse.cineverse.dto.crew;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewMemberDto {
    private int id;
    private String name;
    private String imageUrl;
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;
    private String placeOfBirth;
    private String knownForDepartment;
    private List<String> alsoKnownAs;
}
