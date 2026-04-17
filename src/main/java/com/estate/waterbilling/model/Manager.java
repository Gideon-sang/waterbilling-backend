package com.estate.waterbilling.model;

import jakarta.persistence.*;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // stored as BCrypt hash

    // ── Constructors ──
    public Manager() {}

    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ── Getters & Setters ──
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
