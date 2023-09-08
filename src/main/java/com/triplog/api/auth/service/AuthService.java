package com.triplog.api.auth.service;

import com.triplog.api.auth.dto.TokenRequestDTO;
import com.triplog.api.auth.dto.TokenResponseDTO;
import com.triplog.api.auth.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public TokenResponseDTO login(TokenRequestDTO tokenRequestDTO) {
        //Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenRequestDTO.getEmail(),
                tokenRequestDTO.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //검증된 인증 정보로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }
}
