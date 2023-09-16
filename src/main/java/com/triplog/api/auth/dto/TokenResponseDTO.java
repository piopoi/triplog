package com.triplog.api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class TokenResponseDTO {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
