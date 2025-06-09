package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    private List<ReviewReaction> reactions;

    public Review(int rate, String reviewTitle, String description, Boolean spoiler, User user, Content content) {
        this.rate = rate;
        this.reviewTitle = reviewTitle;
        this.description = description;
        this.spoiler = spoiler;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.user = user;
        this.content = content;
    }
}
