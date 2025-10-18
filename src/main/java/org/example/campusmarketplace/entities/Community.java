package org.example.campusmarketplace.entities;

// entities/model/Community.java

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private boolean isAnonymous = false; // NEW FIELD

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isAnonymous() { return isAnonymous; } // NEW GETTER
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; } // NEW SETTER
}