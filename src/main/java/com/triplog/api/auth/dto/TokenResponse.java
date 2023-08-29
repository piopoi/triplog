package com.triplog.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
@AllArgsConstructor
public class TokenResponse {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
