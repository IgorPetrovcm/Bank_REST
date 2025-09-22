package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull String token) {
}
