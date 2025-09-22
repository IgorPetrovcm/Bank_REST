package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-transfer")
@RequiredArgsConstructor
public class UserTransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> init(@RequestBody @Valid TransferRequest request) {
        return ResponseEntity.ok(transferService.init(request));
    }
}