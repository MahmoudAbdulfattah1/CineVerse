package com.cineverse.cineverse.dto.crew;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectorDto {
    private int id;
    private String name;
    private String imageUrl;
}
