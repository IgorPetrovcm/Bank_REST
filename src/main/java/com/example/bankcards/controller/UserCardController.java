package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardBalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user-card")
@RequiredArgsConstructor
public class UserCardController {
    private final CardService cardService;

    @GetMapping("/{cardId}/balance")
    public ResponseEntity<CardBalanceResponse> getCardBalance(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }

    @GetMapping
    public ResponseEntity<Set<CardResponse>> getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCardsForCurrentUser(pageable));
    }

    @PatchMapping("/{cardId}/request-block")
    public ResponseEntity<CardResponse> requestToBlock(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.requestToBlockCard(cardId));
    }

}
