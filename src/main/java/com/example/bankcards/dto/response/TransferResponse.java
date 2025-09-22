package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(
        @NotNull BigDecimal fromBalance,
        @NotNull BigDecimal toBalance,
        @NotNull BigDecimal amount,
        @NotNull Instant createdAt
) {}
