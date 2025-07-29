package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
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
    @Range(min = 1, max = 10, message = "Rate must be between 1 and 10")
    private int rate;
    @Column(name = "review_title")
    private String reviewTitle;
    private String description;
    private Boolean spoiler;
    private int likeCount = 0;
    private int dislikeCount = 0;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewReaction> reactions;

    public Review(int rate, String reviewTitle, String description, Boolean spoiler, User user, Content content) {
        this.rate = rate;
        this.reviewTitle = reviewTitle;
        this.description = description;
        this.spoiler = spoiler;
        this.user = user;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
