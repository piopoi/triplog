package com.triplog.api.user.dto;

import com.triplog.api.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserGetResponseDTO {

    private final Long id;
    private final String email;
    private final String role;

    @Builder(access = AccessLevel.PRIVATE)
    private UserGetResponseDTO(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static UserGetResponseDTO from(User user) {
        return UserGetResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getValue())
                .build();
    }
}
