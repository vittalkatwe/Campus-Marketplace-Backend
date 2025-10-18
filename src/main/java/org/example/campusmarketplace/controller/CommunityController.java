package org.example.campusmarketplace.controller;

// controller/CommunityController.java (modified - simplified)

import org.example.campusmarketplace.dto.chat.CommunityDto;
import org.example.campusmarketplace.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;
    // Removed JwtUtils as it's not needed for listing communities anymore

    public CommunityController(CommunityService communityService) { // Simplified constructor
        this.communityService = communityService;
    }

    // Endpoint to get all communities - no user context needed
    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAllCommunities() {
        List<CommunityDto> communities = communityService.getAllCommunities();
        return ResponseEntity.ok(communities);
    }

    // Removed /join and /leave endpoints
}