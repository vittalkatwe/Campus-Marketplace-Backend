package org.example.campusmarketplace.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private String SECRET = "96f5e2af1e744a0d0aa50c3d001b9b93241438563b5339326cf834416ad7bf81"; // use at least 32 chars
    private long EXPIRATION = 172800000; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (email.equals(extractedEmail) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String extractEmailFromStompHeader(SimpMessageHeaderAccessor headerAccessor) {
        // Assuming the JWT token is passed as a 'token' header in STOMP CONNECT frame,
        // or as an 'Authorization' header in the nativeHeaders
        String token = (String) headerAccessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return extractEmail(token);
        }
        // Fallback: Sometimes the principal is already set by a WebSocket security config
        if (headerAccessor.getUser() != null) {
            return headerAccessor.getUser().getName(); // This would be the email if it's the principal
        }
        return null; // Or throw exception
    }

}

