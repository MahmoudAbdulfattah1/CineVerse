package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        }
)
public class ReviewReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public ReviewReaction(User user, Review review, ReactionType reactionType) {
        this.user = user;
        this.review = review;
        this.reactionType = reactionType;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
}
