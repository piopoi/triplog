package com.triplog.api.user.dto;

import com.triplog.api.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGetResponseDTO {

    private final Long id;
    private final String email;
    private final String role;

    public static UserGetResponseDTO from(User user) {
        return UserGetResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getValue())
                .build();
    }
}
