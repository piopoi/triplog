package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.USER_EMAIL_EMPTY;
import static com.triplog.api.user.constants.UserConstants.USER_EMAIL_INVALID;
import static com.triplog.api.user.constants.UserConstants.USER_PASSWORD_EMPTY;
import static com.triplog.api.user.constants.UserConstants.USER_PASSWORD_LENGTH_MIN;

import com.triplog.api.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequest {

    @NotBlank(message = USER_EMAIL_EMPTY)
    @Email(message = USER_EMAIL_INVALID)
    private final String email;

    @NotBlank(message = USER_PASSWORD_EMPTY)
    @Size(min = 8, message = USER_PASSWORD_LENGTH_MIN)
    private final String password;

    @Builder
    public UserRequest(String email, String password) {
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
