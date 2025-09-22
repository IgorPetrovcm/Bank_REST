package com.example.bankcards.exception.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MappingMatchTypeException extends RuntimeException
{
    public MappingMatchTypeException(String message) {
        super(message);
    }
}