package com.cineverse.cineverse.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatsDto {
    private int reviewCount;
    private int watchlistCount;
}
