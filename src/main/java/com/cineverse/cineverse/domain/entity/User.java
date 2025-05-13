package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "APP_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "profile_picture")
    private String profilePicture;
    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;
    @OneToMany(mappedBy = "user")
    private Set<Watchlist> watchlists;
}
