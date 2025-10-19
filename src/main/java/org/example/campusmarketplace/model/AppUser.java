package org.example.campusmarketplace.model;

import jakarta.persistence.*;
import org.example.campusmarketplace.entities.Community;
import org.example.campusmarketplace.entities.Item;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private String otp;
    private LocalDateTime otpExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastLoginAt;
    private boolean enabled = false;
    private String bio;
    private String profilePicUrl;


    public AppUser() {}

    public AppUser(Long id, String email, String password, LocalDateTime createdAt, LocalDateTime verifiedAt, LocalDateTime lastLoginAt, String bio, String profilePicUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.verifiedAt = verifiedAt;
        this.lastLoginAt = lastLoginAt;
        this.bio = bio;
        this.profilePicUrl = profilePicUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

}
