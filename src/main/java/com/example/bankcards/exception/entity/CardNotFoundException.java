package com.example.bankcards.exception.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}