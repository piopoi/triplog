package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.*;
import static com.triplog.api.user.domain.User.*;

import com.triplog.api.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserCreateRequest {

    @NotBlank(message = MESSAGE_USER_EMAIL_EMPTY)
    @Email(message = MESSAGE_USER_EMAIL_INVALID)
    private final String email;

    @NotBlank(message = MESSAGE_USER_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_USER_PASSWORD_LENGTH_MIN)
    private final String password;

    @Builder
    public UserCreateRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
