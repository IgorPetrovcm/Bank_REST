package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;

public record CardBalanceRequest(
        @NotNull Long id
) {}
