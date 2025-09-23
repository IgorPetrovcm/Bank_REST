package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CardResponse(
        @NotNull Long id,
        @NotNull Long userId,

        @NotNull String maskedNumber,
        @NotNull String status,
        @NotNull BigDecimal balance
) {}