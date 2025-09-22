package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

public record RefreshAndAccessTokenResponse(
        @NotNull String access_token,
        @NotNull String refresh_token) {
}
