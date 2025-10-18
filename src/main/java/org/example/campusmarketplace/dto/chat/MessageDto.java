package org.example.campusmarketplace.dto.chat;


import java.time.LocalDateTime;

public class MessageDto {
    private String senderEmail; // Use email as username
    private String receiverEmail; // For direct messages
    private Long communityId; // For community messages
    private String content;
    private LocalDateTime timestamp;

    // Getters and Setters
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
