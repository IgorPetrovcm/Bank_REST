package com.example.bankcards.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CardRequest(
        @NotNull Long userId,

        @Pattern(regexp = "[1-9]{16}")
        @NotNull String number,

        @Future
        @JsonFormat(pattern = "yyyy-MM")
        @NotNull YearMonth expiration,

        @DecimalMin(value = "0.0")
        @Positive
        @Digits(integer = 12, fraction = 2)
        @NotNull BigDecimal balance
) {}