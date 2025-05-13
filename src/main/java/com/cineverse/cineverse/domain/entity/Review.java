package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        }
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rate;
    @Column(name = "review_title")
    private String reviewTitle;
    private String description;
    private Boolean spoiler;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
