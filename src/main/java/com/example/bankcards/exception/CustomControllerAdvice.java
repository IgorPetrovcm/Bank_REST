package com.example.bankcards.exception;

import com.example.bankcards.exception.entity.*;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import com.example.bankcards.exception.service.ServiceLogicException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Component
public class CustomControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(CustomControllerAdvice.class);
    private static final DateTimeFormatter F = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @ExceptionHandler(MappingMatchTypeException.class)
    public ResponseEntity<CommonError> handleMatchMapping(MappingMatchTypeException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, req, Map.of());
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<CommonError> handleNotFound(CardNotFoundException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req, Map.of());
    }
    @ExceptionHandler(CardStatusNotFoundException.class)
    public ResponseEntity<CommonError> handleNotFound(CardStatusNotFoundException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req, Map.of());
    }
    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<CommonError> handleNotFound(RefreshTokenNotFoundException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req, Map.of());
    }
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<CommonError> handleNotFound(RefreshTokenInvalidException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req, Map.of());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CommonError> handleNotFound(UserNotFoundException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req, Map.of());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CommonError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, req, Map.of());
    }

    @ExceptionHandler(ServiceLogicException.class)
    public ResponseEntity<CommonError> handleConflict(ServiceLogicException ex, HttpServletRequest req) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, req, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        // можно вытянуть root cause и сделать message информативнее
        return buildResponse("Database constraint violation", HttpStatus.CONFLICT, req,
                Map.of("detail", ex.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "rejected", fe.getRejectedValue(), "message", fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return buildResponse("Validation failed", HttpStatus.BAD_REQUEST, req, Map.of("fieldErrors", fieldErrors));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return buildResponse("Access denied", HttpStatus.FORBIDDEN, req, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonError> handleAny(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, req,
                Map.of("exception", ex.getClass().getSimpleName()));
    }

    private ResponseEntity<CommonError> buildResponse(String message, HttpStatus status, HttpServletRequest req, Map<String,Object> details) {
        var apiErr = new CommonError(
                OffsetDateTime.now(ZoneOffset.UTC).format(F),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                details
        );
        return ResponseEntity.status(status).body(apiErr);
    }
}