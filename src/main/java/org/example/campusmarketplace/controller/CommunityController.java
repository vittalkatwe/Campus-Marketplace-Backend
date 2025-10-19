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

    public CommunityController(CommunityService communityService) { // Simplified constructor
        this.communityService = communityService;
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAllCommunities() {
        List<CommunityDto> communities = communityService.getAllCommunities();
        return ResponseEntity.ok(communities);
    }

}