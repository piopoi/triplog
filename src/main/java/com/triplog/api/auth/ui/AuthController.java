package com.triplog.api.auth.ui;

import com.triplog.api.auth.application.AuthService;
import com.triplog.api.auth.dto.TokenRequest;
import com.triplog.api.auth.dto.TokenResponse;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest tokenRequest) {
        TokenResponse token = authService.login(tokenRequest);
        return ResponseEntity.ok().body(token);
    }
}

