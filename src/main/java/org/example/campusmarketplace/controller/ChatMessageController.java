package org.example.campusmarketplace.controller;


import org.example.campusmarketplace.dto.chat.MessageDto;
import org.example.campusmarketplace.entities.Message;
import org.example.campusmarketplace.service.MessageService;
import org.example.campusmarketplace.security.JwtUtils; // To extract email from JWT
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

@Controller
public class ChatMessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;
    private final JwtUtils jwtUtils; // To extract email from JWT

    public ChatMessageController(SimpMessageSendingOperations messagingTemplate, MessageService messageService, JwtUtils jwtUtils) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.jwtUtils = jwtUtils;
    }

    // Endpoint for sending messages to a community
    // Client sends to /app/chat.sendMessage (or similar)
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String userEmail = jwtUtils.extractEmailFromStompHeader(headerAccessor);
        chatMessage.setSenderEmail(userEmail);
        chatMessage.setTimestamp(LocalDateTime.now());

        // 1. Save the message WITH the real sender
        Message savedMessage = messageService.saveMessage(chatMessage);

        // 2. Convert to a DTO for broadcasting, which applies anonymity rules
        MessageDto broadcastDto = messageService.convertToDto(savedMessage);

        // 3. Broadcast the sanitized DTO
        if (broadcastDto.getCommunityId() != null) {
            messagingTemplate.convertAndSend("/topic/community/" + broadcastDto.getCommunityId(), broadcastDto);
        } else if (broadcastDto.getReceiverEmail() != null) {
            // For DMs, we send a copy to both users
            MessageDto selfDto = messageService.convertToDto(savedMessage); // DTO for sender
            MessageDto recipientDto = messageService.convertToDto(savedMessage); // DTO for receiver

            messagingTemplate.convertAndSendToUser(
                    recipientDto.getReceiverEmail(), "/queue/messages", recipientDto);
            messagingTemplate.convertAndSendToUser(
                    selfDto.getSenderEmail(), "/queue/messages", selfDto);
        }
    }


    // You might want an endpoint for users joining/leaving chats, to broadcast presence
    // However, for this request, just message sending is described.

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setSenderEmail(message.getSender().getEmail());
        if (message.getReceiver() != null) {
            dto.setReceiverEmail(message.getReceiver().getEmail());
        }
        if (message.getCommunity() != null) {
            dto.setCommunityId(message.getCommunity().getId());
        }
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
