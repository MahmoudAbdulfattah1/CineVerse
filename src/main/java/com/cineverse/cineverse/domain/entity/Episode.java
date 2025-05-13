package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Episode extends Content {
    @Column(name = "run_time")
    private int runTime;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;
}
