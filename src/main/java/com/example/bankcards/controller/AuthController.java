package com.example.bankcards.controller;

import com.example.bankcards.dto.request.RefreshTokenRequest;
import com.example.bankcards.dto.request.SignInRequest;
import com.example.bankcards.dto.response.RefreshAndAccessTokenResponse;
import com.example.bankcards.dto.response.RefreshTokenResponse;
import com.example.bankcards.service.security.JwtService;
import com.example.bankcards.service.security.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Вход в систему",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Учетные данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignInRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная аутентификация",
                            content = @Content(schema = @Schema(implementation = RefreshAndAccessTokenResponse.class))
                    )
            }
    )
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.username(), signInRequest.password());
        authenticationManager.authenticate(authRequest);

        var refreshToken = refreshTokenService.generate(signInRequest.username()).token();
        var accessToken = jwtService.generateToken(signInRequest.username());

        return ResponseEntity.ok(new RefreshAndAccessTokenResponse(accessToken, refreshToken));
    }

    @Operation(
            summary = "Обновление токенов",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh токен для обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Токены успешно обновлены",
                            content = @Content(schema = @Schema(implementation = RefreshAndAccessTokenResponse.class))
                    )
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse;
        try {
            refreshTokenResponse = refreshTokenService.validate(refreshTokenRequest.token());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        var accessToken = jwtService.generateToken(refreshTokenResponse.user().username());
        return ResponseEntity.ok(new RefreshAndAccessTokenResponse(accessToken, refreshTokenResponse.token()));
    }
}
