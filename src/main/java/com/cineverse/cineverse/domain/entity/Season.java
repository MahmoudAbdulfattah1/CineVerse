package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Season extends Content {

    @Column(name = "season_number")
    private int seasonNumber;
    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private Set<Episode> episodes;
}
