package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CardBalanceResponse(
        Long id,
        @NotNull BigDecimal balance
) {}