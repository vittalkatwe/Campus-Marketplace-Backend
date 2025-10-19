package org.example.campusmarketplace.controller;


import org.example.campusmarketplace.model.AppUser;
import org.example.campusmarketplace.repo.UserRepo;
import org.example.campusmarketplace.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepo userRepository;
    private final JwtUtils jwtUtils;

    public ProfileController(UserRepo userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public record UserProfileDto(
            String email,
            String bio,
            String profilePicUrl,
            boolean enabled,
            String createdAt,
            String lastLoginAt,
            String verifiedAt
    ) {}


    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal String email) {
        if (email == null) return ResponseEntity.status(401).build();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfileDto dto = new UserProfileDto(
                user.getEmail(),
                user.getBio(),
                user.getProfilePicUrl(),
                user.isEnabled(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null,
                user.getVerifiedAt() != null ? user.getVerifiedAt().toString() : null
        );

        return ResponseEntity.ok(dto);
    }
}

