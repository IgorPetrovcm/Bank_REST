package com.example.bankcards.service;

import com.example.bankcards.config.properties.RefreshTokenProperties;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.RefreshTokenNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.RefreshTokenRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenProperties refreshTokenProperties;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken generate(String username) {
        final var user =  userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        try {
            return findByUser(user);
        }
        catch (RefreshTokenNotFoundException e) {
            var token = new RefreshToken();

            token.setExpiration(Instant.now().plusMillis(
                    refreshTokenProperties.getExpirationInHours() * 60 * 60 * 1000));
            token.setToken(UUID.randomUUID().toString());
            token.setUser(user);

            return refreshTokenRepository.save(token);
        }
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(RefreshTokenNotFoundException::new);
    }

    public RefreshToken findByUser(User user) {
        return refreshTokenRepository.findByUser(user).orElseThrow(RefreshTokenNotFoundException::new);
    }


    public boolean isExpiration(RefreshToken token) {
        if (token.getExpiration().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return true;
        }
        return false;
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
