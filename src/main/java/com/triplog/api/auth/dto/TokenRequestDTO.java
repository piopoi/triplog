package com.triplog.api.auth.dto;

import static com.triplog.api.auth.constants.AuthConstants.*;
import static com.triplog.api.user.domain.User.USER_PASSWORD_LENGTH_MIN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenRequestDTO {

    @NotBlank(message = MESSAGE_AUTH_EMAIL_EMPTY)
    @Email(message = MESSAGE_AUTH_EMAIL_INVALID)
    private final String email;

    @NotBlank(message = MESSAGE_AUTH_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_AUTH_PASSWORD_LENGTH_MIN)
    private final String password;

    @Builder(access = AccessLevel.PRIVATE)
    private TokenRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequestDTO of(String email, String password) {
        return TokenRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
    }

}
