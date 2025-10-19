package org.example.campusmarketplace.controller;

import org.example.campusmarketplace.dto.chat.MessageDto;
import org.example.campusmarketplace.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class DirectMessageController {

    @Autowired
    private final MessageService messageService;

    public DirectMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Get direct messages between authenticated user and another user
    @GetMapping("/dm/{otherUserEmail}")
    public List<MessageDto> getDirectMessages(@AuthenticationPrincipal String email,
                                              @PathVariable String otherUserEmail) {
        return messageService.getDirectMessages(email, otherUserEmail);
    }

    // Get list of users that the authenticated user has exchanged messages with
    @GetMapping("/dm/contacts")
    public List<String> getDMContacts(@AuthenticationPrincipal String email) {
        return messageService.getDMContacts(email);
    }


    // Optional: endpoint for community messages
    @GetMapping("/community/{communityId}")
    public List<MessageDto> getCommunityMessages(@PathVariable Long communityId) {
        return messageService.getCommunityMessages(communityId);
    }
}

