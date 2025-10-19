package org.example.campusmarketplace.controller;

import org.example.campusmarketplace.dto.chat.MessageDto;
import org.example.campusmarketplace.entities.Message;
import org.example.campusmarketplace.service.MessageService;
import org.example.campusmarketplace.security.JwtUtils;
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
    private final JwtUtils jwtUtils;

    public ChatMessageController(SimpMessageSendingOperations messagingTemplate,
                                 MessageService messageService,
                                 JwtUtils jwtUtils) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.jwtUtils = jwtUtils;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto chatMessage,
                            SimpMessageHeaderAccessor headerAccessor) {
        String userEmail = jwtUtils.extractEmailFromStompHeader(headerAccessor);
        chatMessage.setSenderEmail(userEmail);
        chatMessage.setTimestamp(LocalDateTime.now());

        // Save the message with the real sender
        Message savedMessage = messageService.saveMessage(chatMessage);

        // Convert to DTO for broadcasting (applies anonymity rules if needed)
        MessageDto broadcastDto = messageService.convertToDto(savedMessage);

        // Route the message appropriately
        if (broadcastDto.getCommunityId() != null) {
            // Community message - broadcast to topic
            messagingTemplate.convertAndSend(
                    "/topic/community/" + broadcastDto.getCommunityId(),
                    broadcastDto
            );
        } else if (broadcastDto.getReceiverEmail() != null) {
            // Direct message - send to BOTH users using convertAndSendToUser

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                    broadcastDto.getReceiverEmail(),
                    "/queue/messages",
                    broadcastDto
            );

            // Send to sender (so they see their own message)
            messagingTemplate.convertAndSendToUser(
                    broadcastDto.getSenderEmail(),
                    "/queue/messages",
                    broadcastDto
            );
        }
    }
}