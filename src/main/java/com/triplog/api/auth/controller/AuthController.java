package com.triplog.api.auth.controller;

import com.triplog.api.auth.dto.TokenRequestDTO;
import com.triplog.api.auth.dto.TokenResponseDTO;
import com.triplog.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid TokenRequestDTO tokenRequestDTO) {
        TokenResponseDTO tokenResponseDTO = authService.login(tokenRequestDTO);
        return ResponseEntity.ok().body(tokenResponseDTO);
    }
}

