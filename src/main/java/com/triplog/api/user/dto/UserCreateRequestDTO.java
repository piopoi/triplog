package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.*;
import static com.triplog.api.user.domain.User.USER_PASSWORD_LENGTH_MIN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserCreateRequestDTO {

    @NotBlank(message = MESSAGE_USER_EMAIL_EMPTY)
    @Email(message = MESSAGE_USER_EMAIL_INVALID)
    private String email;

    @NotBlank(message = MESSAGE_USER_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_USER_PASSWORD_LENGTH_MIN)
    private String password;

    @NotBlank(message = MESSAGE_USER_ROLE_EMPTY)
    private String role;
}
