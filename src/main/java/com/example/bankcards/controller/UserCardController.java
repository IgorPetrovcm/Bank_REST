package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardBalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user-card")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserCardController {
    private final CardService cardService;

    @Operation(
            summary = "Получить баланс карты",
            parameters = {
                    @Parameter(name = "cardId", description = "ID карты", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение баланса",
                            content = @Content(schema = @Schema(implementation = CardBalanceResponse.class))
                    )
            }
    )
    @GetMapping("/{cardId}/balance")
    public ResponseEntity<CardBalanceResponse> getCardBalance(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }

    @Operation(
            summary = "Получить все карты пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка карт",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CardResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Set<CardResponse>> getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCardsForCurrentUser(pageable));
    }

    @Operation(
            summary = "Запросить блокировку карты",
            parameters = {
                    @Parameter(name = "cardId", description = "ID карты", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на блокировку успешно отправлен",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))
                    )
            }
    )
    @PatchMapping("/{cardId}/request-block")
    public ResponseEntity<CardResponse> requestToBlock(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.requestToBlockCard(cardId));
    }
}
