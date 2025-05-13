package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Movie extends Content {
    @Column(name = "run_time")
    private int runtime;
    @Column(name = "production_country")
    private String productionCountry;
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director;
}
