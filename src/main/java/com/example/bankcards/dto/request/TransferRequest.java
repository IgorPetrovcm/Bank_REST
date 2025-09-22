package com.example.bankcards.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long fromId,

        @NotNull Long toId,

        @DecimalMin(value = "0.0")
        @Positive
        @Digits(integer = 19, fraction = 2)
        @NotNull BigDecimal amount

) {}