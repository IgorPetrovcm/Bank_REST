package com.example.bankcards.controller;

import com.example.bankcards.dto.request.RefreshTokenRequest;
import com.example.bankcards.dto.request.SignInRequest;
import com.example.bankcards.dto.response.RefreshTokenResponse;
import com.example.bankcards.dto.response.SignInResponse;
import com.example.bankcards.service.JwtService;
import com.example.bankcards.service.RefreshTokenService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.username(), signInRequest.password());
        authenticationManager.authenticate(authRequest);

        var refreshToken = refreshTokenService.generate(signInRequest.username()).getToken();
        var accessToken = jwtService.generateToken(signInRequest.username());

        return ResponseEntity.ok(new SignInResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        final var refreshToken = refreshTokenService.findByToken(refreshTokenRequest.token());
        final var user = refreshToken.getUser();

        if (refreshTokenService.isExpiration(refreshToken)) return ResponseEntity.badRequest().body("Refresh token expired");
        if (Objects.isNull(user)) return ResponseEntity.internalServerError().body("Invalid refresh token");

        var accessToken = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new RefreshTokenResponse(accessToken, refreshToken.getToken()));
    }


}
