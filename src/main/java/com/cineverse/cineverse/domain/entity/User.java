package com.cineverse.cineverse.domain.entity;

import com.cineverse.cineverse.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "APP_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column(unique = true)
    private String username;
    @Column(nullable = true)
    private String password;
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String profilePictureUuid;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_oauth2_user")
    private boolean isOauth2User;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    private boolean enabled;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Watchlist> watchlists;

    @PrePersist
    protected void onCreate() {
        enabled = false;
        role = Role.USER;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
