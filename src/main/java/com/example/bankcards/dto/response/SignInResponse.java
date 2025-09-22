package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

public record SignInResponse(
        @NotNull String accessToken,
        @NotNull String refreshToken) {
}
