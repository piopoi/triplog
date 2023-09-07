package com.triplog.api.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenResponseDTO {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private TokenResponseDTO(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenResponseDTO of(String grantType, String accessToken, String refreshToken) {
        return TokenResponseDTO.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
