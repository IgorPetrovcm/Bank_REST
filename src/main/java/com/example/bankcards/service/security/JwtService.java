package com.example.bankcards.service.security;

import com.example.bankcards.config.properties.AccessTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class JwtService {
    private final AccessTokenProperties accessTokenProperties;

    private final SecretKey secretKey;

    public String generateToken(String username) {
        final var now = new Date(System.currentTimeMillis());
        final var expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(
                accessTokenProperties.getExpirationInMinutes()));
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .notBefore(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final var username = extractUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isExpiredToken(token);
    }

    public String extractUsernameFromToken(String token) {
        final var claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    private boolean isExpiredToken(String token) {
        final var claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}