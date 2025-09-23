package com.example.bankcards.exception.service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceLogicException extends RuntimeException {
    public ServiceLogicException(String message) {
        super(message);
    }
}
