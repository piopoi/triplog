package com.triplog.api.auth.ui;

import com.triplog.api.auth.application.AuthService;
import com.triplog.api.auth.dto.TokenRequestDTO;
import com.triplog.api.auth.dto.TokenResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid TokenRequestDTO tokenRequestDTO) {
        TokenResponseDTO tokenResponseDTO = authService.login(tokenRequestDTO);
        return ResponseEntity.ok().body(tokenResponseDTO);
    }
}

