package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.WatchingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(
        name = "watchlist",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        }
)
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    @Enumerated(EnumType.STRING)
    @Column(name = "watching_status")
    private WatchingStatus watchingStatus;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;

}
