package com.example.bankcards.exception;

import java.util.Map;

public record CommonError(
        String timestamp,   // ISO
        int status,         // Http StatusCode
        String error,       // Error summary
        String message,     // Error message
        String path,        // Requested URI
        Map<String, Object> details     // Extra fields
) {}