package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-transfer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserTransferController {
    private final TransferService transferService;

    @Operation(
            summary = "Создать денежный перевод",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Перевод успешно инициирован",
                            content = @Content(schema = @Schema(implementation = TransferResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TransferResponse> init(@RequestBody @Valid TransferRequest request) {
        return ResponseEntity.ok(transferService.init(request));
    }
}