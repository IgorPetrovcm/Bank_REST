package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cards", description = "Инструменты Админа для управления банковскими картами")
public class AdminCardController {
    private final CardService cardService;

    @Operation(
            summary = "Получить все карты с пагинацией",
            parameters = {
        @Parameter(name = "page", description = "Номер страницы (начинается с 0)", example = "0"),
        @Parameter(name = "size", description = "Размер страницы", example = "20"),
        },
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
    public ResponseEntity<Set<CardResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @Operation(
            summary = "Получить карты с запросом на блокировку",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка карт",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CardResponse.class))
                            )
                    ),
            }
    )
    @GetMapping("/block-requested")
    public ResponseEntity<Set<CardResponse>> getAllByIsBlockRequested() {
        return ResponseEntity.ok(cardService.getAllCardsBlockRequested());
    }

    @Operation(
            summary = "Создать новую карту",
            description = "Карта для пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CardRequest cardRequest)
    throws URISyntaxException {
        var cardResponse = cardService.createCard(cardRequest);
        return ResponseEntity.created(new URI(""))
                .body(cardResponse);
    }

    @Operation(
            summary = "Заблокировать карту",
            parameters = {
                    @Parameter(name = "cardId", description = "ID карты", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Карта успешно заблокирована",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))
                    ),
            }
    )
    @PatchMapping("/{cardId}/block")
    public ResponseEntity<CardResponse> cardBlock(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.cardBlock(cardId));
    }


    @Operation(
            summary = "Активировать карту",
            parameters = {
                    @Parameter(name = "cardId", description = "ID карты", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Карта успешно активирована",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))
                    )
            }
    )
    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<CardResponse> cardActivate(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.cardActivate(cardId));
    }


    @Operation(
            summary = "Удалить карту",
            parameters = {
                    @Parameter(name = "cardId", description = "ID карты", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Карта успешно удалена"
                    )
            }
    )
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
