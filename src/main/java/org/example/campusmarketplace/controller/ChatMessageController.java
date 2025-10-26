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
        System.out.println("📨 Received WebSocket message: " + chatMessage);

        String userEmail = jwtUtils.extractEmailFromStompHeader(headerAccessor);

        if (userEmail == null) {
            System.err.println("❌ Authentication failed: Could not extract user email from WebSocket headers");
            return;
        }

        System.out.println("✅ Authenticated user: " + userEmail);

        chatMessage.setSenderEmail(userEmail);
        chatMessage.setTimestamp(LocalDateTime.now());

        try {
            // Save the message with the real sender
            Message savedMessage = messageService.saveMessage(chatMessage);

            // Convert to DTO for broadcasting (applies anonymity rules if needed)
            MessageDto broadcastDto = messageService.convertToDto(savedMessage);

            System.out.println("📤 Broadcasting message: " + broadcastDto);

            // Route the message appropriately
            if (broadcastDto.getCommunityId() != null) {
                // Community message - broadcast to topic
                String topic = "/topic/community/" + broadcastDto.getCommunityId();
                System.out.println("📍 Sending to community topic: " + topic);
                messagingTemplate.convertAndSend(topic, broadcastDto);
            } else if (broadcastDto.getReceiverEmail() != null) {
                // Direct message - send to BOTH users using convertAndSendToUser
                System.out.println("📍 Sending direct message to: " + broadcastDto.getReceiverEmail() + " and " + broadcastDto.getSenderEmail());

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
        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}