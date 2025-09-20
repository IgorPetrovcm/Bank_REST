package com.example.bankcards.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
