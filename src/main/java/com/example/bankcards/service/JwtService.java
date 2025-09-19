package com.example.bankcards.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
public class JwtService {
    private static final int MINUTE_IN_MILLIS = 60 * 1000;

    @Value("${spring.security.access-token.expiration-in-minutes}")
    private static int expirationInMinutes;

    private final SecretKey secretKey;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (MINUTE_IN_MILLIS * expirationInMinutes)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final var username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isExpiredToken(token));
    }

    public String extractUsernameFromToken(String token) {
        final var claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    private boolean isExpiredToken(String token) {
        final var claims = getClaimsFromToken(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}