package com.example.bankcards.service;

import com.example.bankcards.config.properties.RefreshTokenProperties;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.exception.RefreshTokenNotFoundException;
import com.example.bankcards.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenProperties refreshTokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generate(String username) {
        try {
            return findByUsername(username);
        }
        catch (RefreshTokenNotFoundException e) {
            var token = new RefreshToken();

            token.setUsername(username);
            token.setExpiration(Instant.now().plusMillis(
                    refreshTokenProperties.getExpirationInHours() * 60 * 60 * 1000));
            token.setToken(UUID.randomUUID().toString());

            return refreshTokenRepository.save(token);
        }
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(RefreshTokenNotFoundException::new);
    }

    public RefreshToken findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username).orElseThrow(RefreshTokenNotFoundException::new);
    }


    public boolean isExpiration(RefreshToken token) {
        if (token.getExpiration().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return true;
        }
        return false;
    }

    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}
