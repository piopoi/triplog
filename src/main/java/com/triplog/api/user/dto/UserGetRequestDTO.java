package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_EMAIL_EMPTY;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_EMAIL_INVALID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGetRequestDTO {

    @NotBlank(message = MESSAGE_USER_EMAIL_EMPTY)
    @Email(message = MESSAGE_USER_EMAIL_INVALID)
    private String email;

    @Builder(access = AccessLevel.PRIVATE)
    private UserGetRequestDTO(String email) {
        this.email = email;
    }

    public static UserGetRequestDTO from(String email) {
        return UserGetRequestDTO.builder()
                .email(email)
                .build();
    }
}
