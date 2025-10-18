package org.example.campusmarketplace.controller;

import org.example.campusmarketplace.dto.auth.LoginRequestDto;
import org.example.campusmarketplace.dto.auth.RegisterRequestDto;
import org.example.campusmarketplace.dto.auth.VerifyRequestDto;
import org.example.campusmarketplace.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        String email = registerRequestDto.getEmail();
        String password = registerRequestDto.getPassword();
        try {
            return ResponseEntity.ok(authService.register(email, password));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequestDto verifyRequestDto) {
        String email = verifyRequestDto.getEmail();
        String otp = verifyRequestDto.getOtp();
        try {
            return ResponseEntity.ok(authService.verifyOtp(email, otp));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            String token = authService.login(email, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        try {
            return ResponseEntity.ok(authService.resendOtp(email));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

