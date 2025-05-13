package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Series extends Content {
    @Column(name = "number_of_seasons")
    private int numberOfSeasons;
    @Column(name = "number_of_episodes")
    private int numberOfEpisodes;
    private String status;
    @Column(name = "production_country")
    private String productionCountry;
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director;
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<Season> seasons;

}
