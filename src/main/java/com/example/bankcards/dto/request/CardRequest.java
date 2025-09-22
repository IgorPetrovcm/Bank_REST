package com.example.bankcards.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record CardRequest(
        @NotNull Long userId,

        @Pattern(regexp = "[1-9]{16}")
        @NotNull String number,

        @Future
        @NotNull Instant expiration,

        @DecimalMin(value = "0.0")
        @Positive
        @Digits(integer = 12, fraction = 2)
        @NotNull BigDecimal balance
) {}