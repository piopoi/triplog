package com.triplog.api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenResponseDTO {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public TokenResponseDTO(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
