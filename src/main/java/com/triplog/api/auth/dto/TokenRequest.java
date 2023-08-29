package com.triplog.api.auth.dto;

import static com.triplog.api.auth.AuthConstants.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {

    @NotNull(message = AUTH_EMAIL_EMPTY)
    @Email(message = AUTH_EMAIL_INVALID)
    private String email;

    @NotNull(message = AUTH_PASSWORD_EMPTY)
    @Size(min = 8, message = AUTH_PASSWORD_LENGTH_MIN)
    private String password;
}
