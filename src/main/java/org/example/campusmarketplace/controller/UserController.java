package org.example.campusmarketplace.controller;

// controller/UserController.java

import org.example.campusmarketplace.dto.chat.UserDto;
import org.example.campusmarketplace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String email) {
        List<UserDto> users = userService.searchUsersByEmailPartial(email);
        return ResponseEntity.ok(users);
    }
}
