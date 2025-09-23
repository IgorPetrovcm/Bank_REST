package com.example.bankcards.controller;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin-user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @Operation(
            summary = "Получить всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка пользователей",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Set<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }


    @Operation(
            summary = "Удалить пользователя",
            parameters = {
                    @Parameter(name = "userId", description = "ID пользователя", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Пользователь успешно удален"
                    )
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
