package com.example.bankcards.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RefreshTokenResponse(
        @NotNull Long id,
        @NotNull String token,
        @NotNull @JsonIgnore Instant expiration,
        @NotNull UserResponse user
) {
}
