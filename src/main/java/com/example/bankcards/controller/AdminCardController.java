package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@RestController
@RequestMapping("/api/admin-card")
@RequiredArgsConstructor
public class AdminCardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Set<CardResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CardRequest cardRequest)
    throws URISyntaxException {
        var cardResponse = cardService.createCard(cardRequest);
        return ResponseEntity.created(new URI(""))
                .body(cardResponse);
    }

    @PatchMapping("/{cardId}/block")
    public ResponseEntity<CardResponse> cardBlock(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.cardBlock(cardId));
    }

    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<CardResponse> cardActivate(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.cardActivate(cardId));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
