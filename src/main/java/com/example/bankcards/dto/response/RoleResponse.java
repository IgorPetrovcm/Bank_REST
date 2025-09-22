package com.example.bankcards.dto.response;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record RoleResponse(
        @NotNull Long id,
        @NotNull String name
) { }
