package com.example.bankcards.service.security;

import com.example.bankcards.config.properties.RefreshTokenProperties;
import com.example.bankcards.dto.response.RefreshTokenResponse;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.entity.RefreshTokenInvalidException;
import com.example.bankcards.exception.entity.RefreshTokenNotFoundException;
import com.example.bankcards.exception.entity.UserNotFoundException;
import com.example.bankcards.mapper.RefreshTokenMapper;
import com.example.bankcards.repository.RefreshTokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserService userService;

    private final RefreshTokenProperties refreshTokenProperties;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenResponse generate(String username) {
        final var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        try {
            return findByUserId(user.getId());
        }
        catch (RefreshTokenNotFoundException e) {
            var token = new RefreshToken();

            token.setExpiration(Instant.now().plusMillis(
                    refreshTokenProperties.getExpirationInHours() * 60 * 60 * 1000));
            token.setToken(UUID.randomUUID().toString());
            token.setUser(user);

            refreshTokenRepository.save(token);
            return refreshTokenMapper.toDTO(token, RefreshTokenResponse.class);
        }
    }

    public RefreshTokenResponse validate(@NotNull String tokenName) {
        var token = findByToken(tokenName);
        if (token == null) {
            throw new RefreshTokenNotFoundException();
        }
        if (isExpiration(token)) {
            throw new RefreshTokenInvalidException();
        }
        if (token.getUser() == null) {
            throw new RefreshTokenInvalidException();
        }

        return refreshTokenMapper.toDTO(token, RefreshTokenResponse.class);
    }

    private RefreshToken findByToken(String tokenName) {
        return refreshTokenRepository.findByToken(tokenName)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    private RefreshTokenResponse findByUserId(Long userId) {
        var token = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(RefreshTokenNotFoundException::new);
        return refreshTokenMapper.toDTO(token, RefreshTokenResponse.class);
    }


    private boolean isExpiration(RefreshToken token) {
        if (token.getExpiration().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return true;
        }
        return false;
    }

    private void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
