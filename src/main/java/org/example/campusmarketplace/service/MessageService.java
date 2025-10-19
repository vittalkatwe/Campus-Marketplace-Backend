package org.example.campusmarketplace.service;

import org.example.campusmarketplace.model.AppUser;
import org.example.campusmarketplace.entities.Community;
import org.example.campusmarketplace.entities.Message;
import org.example.campusmarketplace.dto.chat.MessageDto;
import org.example.campusmarketplace.repo.UserRepo;
import org.example.campusmarketplace.repo.CommunityRepo;
import org.example.campusmarketplace.repo.MessageRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {

    private final MessageRepo messageRepo;
    private final UserRepo appUserRepo;
    private final CommunityRepo communityRepo;

    public MessageService(MessageRepo messageRepo, UserRepo appUserRepo, CommunityRepo communityRepo) {
        this.messageRepo = messageRepo;
        this.appUserRepo = appUserRepo;
        this.communityRepo = communityRepo;
    }

    public Message saveMessage(MessageDto messageDto) {
        AppUser sender = appUserRepo.findByEmail(messageDto.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + messageDto.getSenderEmail()));

        Message message = new Message();
        message.setSender(sender);
        message.setContent(messageDto.getContent());
        message.setTimestamp(LocalDateTime.now());

        if (messageDto.getCommunityId() != null) {
            Community community = communityRepo.findById(messageDto.getCommunityId())
                    .orElseThrow(() -> new RuntimeException("Community not found: " + messageDto.getCommunityId()));
            message.setCommunity(community);
        } else if (messageDto.getReceiverEmail() != null) {
            AppUser receiver = appUserRepo.findByEmail(messageDto.getReceiverEmail())
                    .orElseThrow(() -> new RuntimeException("Receiver not found: " + messageDto.getReceiverEmail()));
            message.setReceiver(receiver);
        } else {
            throw new IllegalArgumentException("Message must have either a community or a receiver.");
        }

        return messageRepo.save(message);
    }


    public List<MessageDto> getCommunityMessages(Long communityId) {
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found: " + communityId));
        return messageRepo.findByCommunityOrderByTimestampAsc(community).stream()
                .map(this::convertToDto) // USE THE NEW METHOD
                .collect(Collectors.toList());
    }


    public List<MessageDto> getDirectMessages(String user1Email, String user2Email) {
        AppUser user1 = appUserRepo.findByEmail(user1Email)
                .orElseThrow(() -> new RuntimeException("User not found: " + user1Email));
        AppUser user2 = appUserRepo.findByEmail(user2Email)
                .orElseThrow(() -> new RuntimeException("User not found: " + user2Email));

        List<Message> messages1 = messageRepo.findBySenderAndReceiverOrderByTimestampAsc(user1, user2);
        List<Message> messages2 = messageRepo.findByReceiverAndSenderOrderByTimestampAsc(user1, user2);

        return Stream.concat(messages1.stream(), messages2.stream())
                .sorted(Comparator.comparing(Message::getTimestamp))
                .map(this::convertToDto) // USE THE NEW METHOD
                .collect(Collectors.toList());

    }

    public MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());

        Community community = message.getCommunity();
        if (community != null) {
            dto.setCommunityId(community.getId());
            // ANONYMITY RULE: If the community is anonymous, scrub the sender's email.
            if (community.isAnonymous()) {
                dto.setSenderEmail("Anonymous"); // Send a generic label
            } else {
                dto.setSenderEmail(message.getSender().getEmail());
            }
        } else { // This is a Direct Message
            dto.setSenderEmail(message.getSender().getEmail());
            if (message.getReceiver() != null) {
                dto.setReceiverEmail(message.getReceiver().getEmail());
            }
        }
        return dto;
    }

    public List<String> getDMContacts(String email) {
        // Fetch distinct users you have sent/received messages with
        return messageRepo.findDistinctChatPartners(email);
    }




}
