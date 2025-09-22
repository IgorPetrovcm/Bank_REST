package com.example.bankcards.exception.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}