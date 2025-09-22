package com.example.bankcards.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserResponse(
        @NotNull Long id,
        @NotNull String username,
        @NotNull @JsonIgnore String password,
        @NotNull @JsonIgnore Set<RoleResponse> roles,
        @JsonIgnore Set<RefreshTokenResponse> refreshTokenResponses
) {}