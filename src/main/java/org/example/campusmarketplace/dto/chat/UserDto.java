package org.example.campusmarketplace.dto.chat;

public class UserDto {
    private Long id;
    private String email;
    private String profilePicUrl; // Optional

    public UserDto() {}
    public UserDto(Long id, String email, String profilePicUrl) {
        this.id = id;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }
}
