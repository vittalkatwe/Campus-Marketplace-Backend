package org.example.campusmarketplace.service;

// service/UserService.java (or integrate into an existing service)

import org.example.campusmarketplace.model.AppUser;
import org.example.campusmarketplace.dto.chat.UserDto;
import org.example.campusmarketplace.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService { // Or simply add this to your existing AuthService if it handles user details

    private final UserRepo appUserRepo;

    public UserService(UserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public AppUser getAppUserByEmail(String email) {
        return appUserRepo.findByEmail(email).orElse(null);
    }

    public List<UserDto> searchUsersByEmailPartial(String emailPartial) {
        // Implement a custom query in AppUserRepo for LIKE search
        // For simplicity, this example fetches all and filters, but a DB query is better.
        return appUserRepo.findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(emailPartial.toLowerCase()))
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getProfilePicUrl()))
                .collect(Collectors.toList());
    }

    // You'll want to add a method to AppUserRepo:
    // List<AppUser> findByEmailContainingIgnoreCase(String emailPartial);
    // And then use that here directly.
}
