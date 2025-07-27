package com.cineverse.cineverse.dto.content;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentCastDto {
    private int id;
    private String characterName;
    private String name;
    private String imageUrl;
}
