package org.example.campusmarketplace.entities;

// entities/model/Message.java

import jakarta.persistence.*;
import org.example.campusmarketplace.model.AppUser;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id") // For direct messages
    private AppUser receiver;

    @ManyToOne
    @JoinColumn(name = "community_id") // For community messages
    private Community community;

    private String content;
    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AppUser getSender() { return sender; }
    public void setSender(AppUser sender) { this.sender = sender; }
    public AppUser getReceiver() { return receiver; }
    public void setReceiver(AppUser receiver) { this.receiver = receiver; }
    public Community getCommunity() { return community; }
    public void setCommunity(Community community) { this.community = community; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
