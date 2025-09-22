package com.example.bankcards.exception.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) { super(message); }
}

