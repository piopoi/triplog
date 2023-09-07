package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_EMAIL_EMPTY;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_EMAIL_INVALID;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_PASSWORD_EMPTY;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_PASSWORD_LENGTH_MIN;
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
public class UserCreateRequestDTO {

    @NotBlank(message = MESSAGE_USER_EMAIL_EMPTY)
    @Email(message = MESSAGE_USER_EMAIL_INVALID)
    private final String email;

    @NotBlank(message = MESSAGE_USER_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_USER_PASSWORD_LENGTH_MIN)
    private final String password;

    @Builder(access = AccessLevel.PRIVATE)
    private UserCreateRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCreateRequestDTO of(String email, String password) {
        return UserCreateRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
    }
}
