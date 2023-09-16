package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_PASSWORD_EMPTY;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_PASSWORD_LENGTH_MIN;
import static com.triplog.api.user.domain.User.USER_PASSWORD_LENGTH_MIN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PasswordUpdateRequestDTO {

    @NotBlank(message = MESSAGE_USER_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_USER_PASSWORD_LENGTH_MIN)
    private String password;
}
