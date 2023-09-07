package com.triplog.api.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.triplog.api.auth.dto.TokenRequestDTO;
import com.triplog.api.auth.dto.TokenResponseDTO;
import com.triplog.api.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("로그인 할 수 있다")
    void login() {
        //given
        TokenRequestDTO tokenRequestDTO = TokenRequestDTO.of("test@example.com", "password");
        TokenResponseDTO expectedResponse = TokenResponseDTO.of("Bearer", "TOKEN", "TOKEN");
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(jwtTokenProvider.generateToken(authentication)).willReturn(expectedResponse);

        //when
        TokenResponseDTO actualResponse = authService.login(tokenRequestDTO);

        //then
        assertThat(actualResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken());
    }
}
