package org.example.campusmarketplace.service;

// service/CommunityService.java (modified - simplified)

import org.example.campusmarketplace.entities.Community;
import org.example.campusmarketplace.dto.chat.CommunityDto;
import org.example.campusmarketplace.repo.CommunityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;

@Service
public class CommunityService {

    private final CommunityRepo communityRepo;

    public CommunityService(CommunityRepo communityRepo) {
        this.communityRepo = communityRepo;
    }


    // Simpler getAllCommunities, no need for user context
    public List<CommunityDto> getAllCommunities() {
        return communityRepo.findAll().stream()
                .map(community -> new CommunityDto(
                        community.getId(),
                        community.getName(),
                        community.getDescription(),
                        community.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Community> getCommunityById(Long communityId) {
        return communityRepo.findById(communityId);
    }

    @PostConstruct
    public void initCommunities() {
        // Using a Map for cleaner initialization
        Map<String, Boolean> communityConfig = Map.of(
                "Study", false,
                "Events", false,
                "Confessions", true, // Mark as anonymous
                "Help", true,      // Mark as anonymous
                "Regular Convo", false,
                "Trips", false
        );

        communityConfig.forEach((name, isAnonymous) -> {
            if (communityRepo.findByName(name).isEmpty()) {
                Community community = new Community();
                community.setName(name);
                community.setDescription("Community for " + name);
                community.setCreatedAt(LocalDateTime.now());
                community.setAnonymous(isAnonymous); // SET THE FLAG HERE
                communityRepo.save(community);
            }
        });
    }


    // Removed joinCommunity and leaveCommunity methods
}