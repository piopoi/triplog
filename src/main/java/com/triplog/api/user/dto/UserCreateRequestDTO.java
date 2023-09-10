package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.*;
import static com.triplog.api.user.domain.User.USER_PASSWORD_LENGTH_MIN;

import com.triplog.api.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateRequestDTO {

    @NotBlank(message = MESSAGE_USER_EMAIL_EMPTY)
    @Email(message = MESSAGE_USER_EMAIL_INVALID)
    private String email;

    @NotBlank(message = MESSAGE_USER_PASSWORD_EMPTY)
    @Size(min = USER_PASSWORD_LENGTH_MIN, message = MESSAGE_USER_PASSWORD_LENGTH_MIN)
    private String password;

    private String role;

    @Builder(access = AccessLevel.PRIVATE)
    private UserCreateRequestDTO(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserCreateRequestDTO of(String email, String password) {
        return UserCreateRequestDTO.builder()
                .email(email)
                .password(password)
                .role(Role.USER.name())
                .build();
    }

    public static UserCreateRequestDTO of(String email, String password, String role) {
        return UserCreateRequestDTO.builder()
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}
