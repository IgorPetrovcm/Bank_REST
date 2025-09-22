package com.example.bankcards.exception.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CardStatusNotFoundException extends RuntimeException {
    public CardStatusNotFoundException(String message) {
        super(message);
    }
}
